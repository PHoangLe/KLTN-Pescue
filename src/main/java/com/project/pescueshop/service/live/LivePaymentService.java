package com.project.pescueshop.service.live;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.project.pescueshop.model.dto.*;
import com.project.pescueshop.model.elastic.ElasticClient;
import com.project.pescueshop.model.elastic.Index;
import com.project.pescueshop.model.elastic.document.InvoiceData;
import com.project.pescueshop.model.entity.*;
import com.project.pescueshop.model.entity.live.*;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.*;
import com.project.pescueshop.util.Util;
import com.project.pescueshop.util.constant.EnumInvoiceStatus;
import com.project.pescueshop.util.constant.EnumPaymentType;
import com.project.pescueshop.util.constant.EnumResponseCode;
import com.project.pescueshop.util.constant.EnumVoucherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class LivePaymentService {
    private final static long MEMBER_POINT_RATE = 20L;
    private final LiveInvoiceService invoiceService;
    private final LiveCartService cartService;
    private final LiveItemService liveItemService;
    private final VarietyService varietyService;
    private final MerchantService merchantService;
    private final UserService userService;
    private final ShippingFeeService shippingFeeService;
    private final PaymentService paymentService;
    private final ThreadService threadService;

    public CheckoutResultDTO userCartCheckout(User user, CartCheckOutInfoDTO cartCheckOutInfoDTO) throws FriendlyException {
        PaymentInfoDTO paymentInfo = cartCheckOutInfoDTO.getPaymentInfoDTO();
        EnumPaymentType paymentType = EnumPaymentType.getByValue(paymentInfo.getPaymentType());
        Address address = paymentInfo.getAddress();

        LiveCart cart = cartService.findCartByCartId(cartCheckOutInfoDTO.getCartId());

        if (cart == null){
            throw new FriendlyException(EnumResponseCode.CART_NOT_FOUND);
        }

        if (cart.getLiveCartItemList().isEmpty()){
            throw new FriendlyException(EnumResponseCode.NO_ITEM_TO_CHECKOUT);
        }

        List<LiveCartItem> liveCartItems = cart.getLiveCartItemList();
        List<LiveInvoiceItem> liveInvoiceItems = liveCartItems.stream().map(liveCartItem -> LiveInvoiceItem.builder()
                .liveItemId(liveCartItem.getLiveItem().getLiveItemId())
                .quantity(liveCartItem.getQuantity())
                .totalPrice(liveCartItem.getTotalItemPrice())
                .build()).toList();

        CompletableFuture<Long> shippingFeeFuture = CompletableFuture.supplyAsync(() -> {
            Merchant merchant = merchantService.getMerchantById(cart.getMerchantId());
            return shippingFeeService.calculateShippingFeeForLive(liveCartItems, address, merchant);
        });

        LiveInvoice liveInvoice = LiveInvoice.builder()
                .paymentType(paymentType.getValue())
                .userId(user.getUserId())
                .merchantId(cart.getMerchantId())
                .cityName(address.getCityName())
                .liveSessionId(cart.getSessionId())
                .districtName(address.getDistrictName())
                .districtId(address.getDistrictId())
                .voucher(paymentInfo.getVoucherByMerchantMap().get(cart.getMerchantId()))
                .wardName(address.getWardName())
                .wardCode(address.getWardCode())
                .streetName(address.getStreetName())
                .status(EnumInvoiceStatus.PENDING.getValue())
                .phoneNumber(paymentInfo.getPhoneNumber())
                .createdDate(Util.getCurrentDate())
                .shippingFee(paymentInfo.getShippingFee())
                .userEmail(paymentInfo.getUserEmail())
                .recipientName(paymentInfo.getRecipientName())
                .build();

        long invoiceValue = liveCartItems.stream().mapToLong(LiveCartItem::getTotalItemPrice).sum();
        try {
            long shippingFee = shippingFeeFuture.get();
            liveInvoice.setTotalPrice(invoiceValue);
            liveInvoice.setShippingFee(shippingFee);
            liveInvoice.setFinalPrice(invoiceValue + shippingFee);
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage());
            throw new FriendlyException(EnumResponseCode.SHIPPING_FEE_ERROR);
        }

        if (liveInvoice.getVoucher() != null){
            Voucher voucher = liveInvoice.getVoucher();
            EnumVoucherType voucherType = EnumVoucherType.getByValue(voucher.getType());
            long discountAmount;

            if (voucherType == EnumVoucherType.PERCENTAGE){
                discountAmount = (voucher.getValue() * liveInvoice.getFinalPrice()) / 100L;
            }
            else {
                discountAmount = voucher.getValue();
            }

            discountAmount = Math.min(discountAmount, voucher.getMaxValue());
            liveInvoice.setDiscountPrice(discountAmount);

            long finalPrice = Math.max(liveInvoice.getFinalPrice() - discountAmount, 0);
            liveInvoice.setFinalPrice(finalPrice);

            CompletableFuture.runAsync(() -> {
                userService.removeMemberPoint(user, voucher.getPrice());
            });
        }

        invoiceService.saveAndFlushLiveInvoice(liveInvoice);

        invoiceService.saveAndFlushLiveInvoice(liveInvoice);
        CompletableFuture.runAsync(() -> addInvoiceItemsToInvoice(liveInvoice, liveInvoiceItems));
        CompletableFuture.runAsync(() -> userService.addMemberPoint(user, liveInvoice.getFinalPrice() / MEMBER_POINT_RATE));
        CompletableFuture.runAsync(() -> cartService.removeSelectedCartItem(cartCheckOutInfoDTO.getCartId()));
        CompletableFuture.runAsync(() -> {
            try {
                threadService.sendReceiptEmail(liveInvoice);
            } catch (FriendlyException e) {
                throw new RuntimeException(e);
            }

            try {
                pushDataToElastic(liveInvoice, liveInvoiceItems);
            } catch (IOException e) {
                log.error("error push invoice data: {}", e.getMessage());
            }
        });

        return createCheckoutResultDTO(paymentType, paymentInfo, cartCheckOutInfoDTO, liveInvoice);
    }

    public CheckoutResultDTO singleItemCheckOut(User user, SingleLiveItemCheckOutInfoDTO info) throws UnsupportedEncodingException, FriendlyException {
        PaymentInfoDTO paymentInfo = info.getPaymentInfoDTO();
        EnumPaymentType paymentType = EnumPaymentType.getByValue(paymentInfo.getPaymentType());
        Address address = paymentInfo.getAddress();

        LiveItem liveItem = liveItemService.findByLiveItemId(info.getLiveItemId());
        Variety variety = varietyService.findById(liveItem.getVarietyId());
        if (variety == null){
            throw new FriendlyException(EnumResponseCode.VARIETY_NOT_FOUND);
        }

        LiveInvoice invoice = new LiveInvoice();
        invoice.setPaymentType(paymentType.getValue());
        invoice.setUserId(user.getUserId());
        invoice.setMerchantId(variety.getMerchantId());
        invoice.setCityName(address.getCityName());
        invoice.setDistrictName(address.getDistrictName());
        invoice.setWardName(address.getWardName());
        invoice.setStreetName(address.getStreetName());
        invoice.setStatus(EnumInvoiceStatus.PENDING.getValue());
        invoice.setPhoneNumber(paymentInfo.getPhoneNumber());
        invoice.setCreatedDate(Util.getCurrentDate());
        invoice.setVoucher(paymentInfo.getVoucherByMerchantMap().get(variety.getMerchantId()));
        invoice.setShippingFee(paymentInfo.getShippingFee());
        invoice.setUserEmail(paymentInfo.getUserEmail());
        invoice.setRecipientName(paymentInfo.getRecipientName());

        Merchant merchant = merchantService.getMerchantById(variety.getMerchantId());

        long invoiceValue = (variety.getPrice() * info.getQuantity());
        invoice.setTotalPrice(invoiceValue);
        invoice.setFinalPrice(invoiceValue + shippingFeeService.calculateShippingFee(variety, address, merchant));

        if (invoice.getVoucher() != null){
            Voucher voucher = invoice.getVoucher();
            EnumVoucherType voucherType = EnumVoucherType.getByValue(voucher.getType());
            long discountAmount = 0;

            if (voucherType == EnumVoucherType.PERCENTAGE){
                discountAmount = (voucher.getValue() * invoice.getTotalPrice()) / 100L;
            }
            else {
                discountAmount = voucher.getValue();
            }

            discountAmount = Math.min(discountAmount, voucher.getMaxValue());
            invoice.setDiscountPrice(discountAmount);

            long finalPrice = Math.max(invoice.getFinalPrice() - discountAmount, 0);
            invoice.setFinalPrice(finalPrice);

            CompletableFuture.runAsync(() -> {
                userService.removeMemberPoint(user, voucher.getPrice());
            });
        }

        invoiceService.saveAndFlushLiveInvoice(invoice);


        CompletableFuture.runAsync(() -> {
            userService.addMemberPoint(user, invoice.getFinalPrice() / MEMBER_POINT_RATE);
          try {
            threadService.sendReceiptEmail(invoice);
          } catch (FriendlyException e) {
            throw new RuntimeException(e);
          }

          LiveInvoiceItem item = LiveInvoiceItem.builder()
                    .liveItem(liveItem)
                    .quantity(info.getQuantity())
                    .liveInvoiceId(invoice.getLiveInvoiceId())
                    .liveItemId(variety.getVarietyId())
                    .totalPrice(variety.getPrice() * info.getQuantity())
                    .build();

            invoiceService.saveAndFlushLiveInvoiceItem(item);
            try {
                pushDataToElastic(invoice, List.of(item));
            } catch (IOException e) {
                log.error("error push invoice data: {}", e.getMessage());
            }
        });

        if (paymentType == EnumPaymentType.CREDIT_CARD){
            return CheckoutResultDTO.builder()
                    .invoiceIdList(List.of(invoice.getLiveInvoiceId()))
                    .paymentUrl(paymentService.createPaymentLink("Invoice ID: " + invoice.getLiveInvoiceId(), paymentInfo.getReturnUrl(), invoice.getFinalPrice()))
                    .build();
        }
        invoiceService.saveAndFlushLiveInvoice(invoice);
        return CheckoutResultDTO.builder()
                .invoiceIdList(List.of(invoice.getLiveInvoiceId()))
                .build();
    }

    private CheckoutResultDTO createCheckoutResultDTO(EnumPaymentType paymentType, PaymentInfoDTO paymentInfo, CartCheckOutInfoDTO cartCheckOutInfoDTO, LiveInvoice liveInvoice) {
        if (paymentType == EnumPaymentType.CREDIT_CARD){
            try {
                return CheckoutResultDTO.builder()
                        .invoiceIdList(List.of(liveInvoice.getLiveInvoiceId()))
                        .paymentUrl(paymentService.createPaymentLink("InvoiceId: " + liveInvoice.getLiveInvoiceId(), paymentInfo.getReturnUrl(), liveInvoice.getFinalPrice()))
                        .cartId(cartCheckOutInfoDTO.getCartId())
                        .build();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return CheckoutResultDTO.builder()
                .invoiceIdList(List.of(liveInvoice.getLiveInvoiceId()))
                .cartId(cartCheckOutInfoDTO.getCartId())
                .build();
    }

    private void addInvoiceItemsToInvoice(LiveInvoice liveInvoice, List<LiveInvoiceItem> invoiceItemDTO) {
        invoiceService.addInvoiceItemsToInvoice(liveInvoice, invoiceItemDTO);
    }

    public CheckoutResultDTO userCartCheckoutAuthenticate(User user, CartCheckOutInfoDTO cartCheckOutInfoDTO) throws FriendlyException {
        return userCartCheckout(user, cartCheckOutInfoDTO);
    }

    public CheckoutResultDTO singleItemCheckOutAuthenticate(User user, SingleLiveItemCheckOutInfoDTO singleItemCheckOutInfoDTO) throws UnsupportedEncodingException, FriendlyException {
        return singleItemCheckOut(user, singleItemCheckOutInfoDTO);
    }

    public void pushDataToElastic(LiveInvoice invoice, List<LiveInvoiceItem> invoiceItems) throws IOException {
        ElasticsearchClient esClient = ElasticClient.get();

        BulkRequest.Builder br = new BulkRequest.Builder();
        for (LiveInvoiceItem invoiceItem : invoiceItems){
            br.operations(op -> op
                    .index(idx -> idx
                            .index(Index.getIndexName(Index.Name.INVOICE_DATA))
                            .id(invoiceItem.getLiveInvoiceId() + "_" + invoiceItem.getLiveItem().getProductId())
                            .document(InvoiceData.builder()
                                    .productId(invoiceItem.getLiveItem().getProductId())
                                    .merchantId(invoice.getMerchantId())
                                    .timestamp(invoice.getCreatedDate().getTime())
                                    .totalAmount(invoiceItem.getTotalPrice())
                                    .invoiceId(invoiceItem.getLiveInvoiceId())
                                    .userId(invoice.getUserId()).build()
                            )
                    )
            );
        }

        BulkResponse result = esClient.bulk(br.build());
        if (result.errors()) {
            log .error("Bulk had errors");
            for (BulkResponseItem item: result.items()) {
                if (item.error() != null) {
                    log.error(item.error().reason());
                }
            }
        }
    }

    public List<ShippingFeeDTO> getShippingFeeByCartId(GetCartShippingFeeRequest request) throws FriendlyException {
        LiveCart cart = cartService.findCartByCartId(request.getCartId());

        if (cart == null){
            throw new FriendlyException(EnumResponseCode.CART_NOT_FOUND);
        }

        if (cart.getLiveCartItemList().isEmpty()){
            throw new FriendlyException(EnumResponseCode.NO_ITEM_TO_CHECKOUT);
        }

        List<LiveCartItem> liveCartItems = cart.getLiveCartItemList();
        Merchant merchant = merchantService.getMerchantById(cart.getMerchantId());
        Address address = Address.builder()
                .districtId(request.getDistrictId())
                .wardCode(request.getWardCode())
                .build();

        return List.of(ShippingFeeDTO.builder()
                        .merchantId(merchant.getMerchantId())
                        .shippingFee(shippingFeeService.calculateShippingFeeForLive(liveCartItems, address, merchant))
                        .build());
    }
}
