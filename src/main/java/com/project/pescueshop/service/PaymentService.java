package com.project.pescueshop.service;

import com.project.pescueshop.config.PaymentConfig;
import com.project.pescueshop.model.dto.CartCheckOutInfoDTO;
import com.project.pescueshop.model.dto.CheckoutResultDTO;
import com.project.pescueshop.model.dto.PaymentInfoDTO;
import com.project.pescueshop.model.dto.SingleItemCheckOutInfoDTO;
import com.project.pescueshop.model.entity.*;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.CartDAO;
import com.project.pescueshop.repository.dao.PaymentDAO;
import com.project.pescueshop.util.Util;
import com.project.pescueshop.util.constant.EnumInvoiceStatus;
import com.project.pescueshop.util.constant.EnumPaymentType;
import com.project.pescueshop.util.constant.EnumResponseCode;
import com.project.pescueshop.util.constant.EnumVoucherType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final static long MEMBER_POINT_RATE = 20L;
    private final PaymentDAO paymentDAO;
    private final CartDAO cartDAO;
    private final VarietyService varietyService;
    private final UserService userService;
    private final ThreadService threadService;

    public String createPaymentLink(String content, String returnUrl, long value) throws UnsupportedEncodingException {

        long amount = value * 100;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", PaymentConfig.vnp_Version);
        vnp_Params.put("vnp_Command", PaymentConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", PaymentConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_Locale", "en");
        vnp_Params.put("vnp_TxnRef", content);
        vnp_Params.put("vnp_OrderInfo", content);
        vnp_Params.put("vnp_OrderType", PaymentConfig.orderType);
        vnp_Params.put("vnp_ReturnUrl", returnUrl);
        vnp_Params.put("vnp_IpAddr", "127.0.0.1");

        ZonedDateTime createDate = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));

        // Format ZonedDateTime using DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String vnp_CreateDate = createDate.format(formatter);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        ZonedDateTime expireDate = createDate.plusHours(2);
        String vnp_ExpireDate = expireDate.format(formatter);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = PaymentConfig.vnp_PayUrl + "?" + queryUrl;

        return paymentUrl;
    }

    public CheckoutResultDTO userCartCheckout(User user, CartCheckOutInfoDTO cartCheckOutInfoDTO) throws UnsupportedEncodingException, FriendlyException {
        PaymentInfoDTO paymentInfo = cartCheckOutInfoDTO.getPaymentInfoDTO();
        EnumPaymentType paymentType = EnumPaymentType.getByValue(paymentInfo.getPaymentType());
        Address address = paymentInfo.getAddress();

        Invoice invoice = new Invoice();
        invoice.setPaymentType(paymentType.getValue());
        invoice.setUserId(user.getUserId());
        invoice.setCityName(address.getCityName());
        invoice.setDistrictName(address.getDistrictName());
        invoice.setWardName(address.getWardName());
        invoice.setStreetName(address.getStreetName());
        invoice.setStatus(EnumInvoiceStatus.PENDING.getValue());
        invoice.setPhoneNumber(paymentInfo.getPhoneNumber());
        invoice.setCreatedDate(Util.getCurrentDate());
        invoice.setVoucher(paymentInfo.getVoucher());
        invoice.setShippingFee(paymentInfo.getShippingFee());
        invoice.setUserEmail(paymentInfo.getUserEmail());
        invoice.setRecipientName(paymentInfo.getRecipientName());

        long invoiceValue = cartDAO.sumValueOfAllSelectedProductInCart(cartCheckOutInfoDTO.getCartId(), user.getUserId());

        if (invoiceValue == 0){
            throw new FriendlyException(EnumResponseCode.NO_ITEM_TO_CHECKOUT);
        }

        invoice.setTotalPrice(invoiceValue);
        invoice.setFinalPrice(invoiceValue + paymentInfo.getShippingFee());

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

        paymentDAO.saveAndFlushInvoice(invoice);

        threadService.sendReceiptEmail(invoice);

        CompletableFuture.runAsync(() -> {
            addInvoiceItemsToInvoice(invoice);
        });

        CompletableFuture.runAsync(() -> {
            userService.addMemberPoint(user, invoice.getFinalPrice() / MEMBER_POINT_RATE);
        });

        CompletableFuture.runAsync(() -> {
            cartDAO.removeSelectedCartItem(cartCheckOutInfoDTO.getCartId());
        });

        if (paymentType == EnumPaymentType.CREDIT_CARD){
            return CheckoutResultDTO.builder()
                    .paymentUrl(createPaymentLink("Invoice ID: " + invoice.getInvoiceId(), paymentInfo.getReturnUrl(), invoice.getFinalPrice()))
                    .invoiceId(invoice.getInvoiceId())
                    .build();
        }
        paymentDAO.saveAndFlushInvoice(invoice);
        return CheckoutResultDTO.builder()
                .invoiceId(invoice.getInvoiceId())
                .build();
    }

    public CheckoutResultDTO singleItemCheckOut(User user, SingleItemCheckOutInfoDTO info) throws UnsupportedEncodingException {
        PaymentInfoDTO paymentInfo = info.getPaymentInfoDTO();
        EnumPaymentType paymentType = EnumPaymentType.getByValue(paymentInfo.getPaymentType());
        Address address = paymentInfo.getAddress();

        Invoice invoice = new Invoice();
        invoice.setPaymentType(paymentType.getValue());
        invoice.setUserId(user.getUserId());
        invoice.setCityName(address.getCityName());
        invoice.setDistrictName(address.getDistrictName());
        invoice.setWardName(address.getWardName());
        invoice.setStreetName(address.getStreetName());
        invoice.setStatus(EnumInvoiceStatus.PENDING.getValue());
        invoice.setPhoneNumber(paymentInfo.getPhoneNumber());
        invoice.setCreatedDate(Util.getCurrentDate());
        invoice.setVoucher(paymentInfo.getVoucher());
        invoice.setShippingFee(paymentInfo.getShippingFee());
        invoice.setUserEmail(paymentInfo.getUserEmail());
        invoice.setRecipientName(paymentInfo.getRecipientName());

        Variety variety = varietyService.findById(info.getVarietyId());
        long invoiceValue = (variety.getPrice() * info.getQuantity());
        invoice.setTotalPrice(invoiceValue);
        invoice.setFinalPrice(invoiceValue + paymentInfo.getShippingFee());

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

        paymentDAO.saveAndFlushInvoice(invoice);

        threadService.sendReceiptEmail(invoice);

        CompletableFuture.runAsync(() -> {
            userService.addMemberPoint(user, invoice.getFinalPrice() / MEMBER_POINT_RATE);

            InvoiceItem item = InvoiceItem.builder()
                    .variety(variety)
                    .quantity(info.getQuantity())
                    .invoiceId(invoice.getInvoiceId())
                    .varietyId(variety.getVarietyId())
                    .totalPrice(variety.getPrice() * info.getQuantity())
                    .build();

            paymentDAO.saveAndFlushItem(item);
        });

        if (paymentType == EnumPaymentType.CREDIT_CARD){
            return CheckoutResultDTO.builder()
                    .invoiceId(invoice.getInvoiceId())
                    .paymentUrl(createPaymentLink("Invoice ID: " + invoice.getInvoiceId(), paymentInfo.getReturnUrl(), invoice.getFinalPrice()))
                    .build();
        }
        paymentDAO.saveAndFlushInvoice(invoice);
        return CheckoutResultDTO.builder()
                .invoiceId(invoice.getInvoiceId())
                .build();
    }

    public void addInvoiceItemsToInvoice(Invoice invoice) {
        cartDAO.addInvoiceItemsToInvoice(invoice);
    }

    public CheckoutResultDTO userCartCheckoutUnAuthenticate(CartCheckOutInfoDTO cartCheckOutInfoDTO) throws FriendlyException, UnsupportedEncodingException {
        User user = userService.getAdminUser();
        return userCartCheckout(user, cartCheckOutInfoDTO);
    }

    public CheckoutResultDTO singleItemCheckOutUnAuthenticate(SingleItemCheckOutInfoDTO singleItemCheckOutInfoDTO) throws FriendlyException, UnsupportedEncodingException {
        User user = userService.getAdminUser();
        return singleItemCheckOut(user, singleItemCheckOutInfoDTO);
    }

    public CheckoutResultDTO userCartCheckoutAuthenticate(User user, CartCheckOutInfoDTO cartCheckOutInfoDTO) throws UnsupportedEncodingException, FriendlyException {
        return userCartCheckout(user, cartCheckOutInfoDTO);
    }

    public CheckoutResultDTO singleItemCheckOutAuthenticate(User user, SingleItemCheckOutInfoDTO singleItemCheckOutInfoDTO) throws UnsupportedEncodingException {
        return singleItemCheckOut(user, singleItemCheckOutInfoDTO);
    }

    public List<InvoiceItem> findInvoiceItemByInvoiceId(String invoiceId) {
        return paymentDAO.findInvoiceItemByInvoiceId(invoiceId);
    }
}
