package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.*;
import com.project.pescueshop.model.entity.*;
import com.project.pescueshop.model.entity.live.LiveInvoice;
import com.project.pescueshop.model.entity.live.LiveInvoiceItem;
import com.project.pescueshop.model.entity.live.LiveItem;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.LiveInvoiceDAO;
import com.project.pescueshop.repository.dao.ViewAuditLogDAO;
import com.project.pescueshop.util.constant.EnumObjectType;
import com.project.pescueshop.util.constant.EnumStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Slf4j
public class ThreadService extends BaseService {
    private final VarietyService varietyService;
    private final ImportService importService;
    private final RatingService ratingService;
    private final ProductService productService;
    private final CartService cartService;
    private final ChatRoomService chatRoomService;
    private final ViewAuditLogDAO viewAuditLogDAO;
    private final FileUploadService fileUploadService;
    private final EmailService emailService;
    private final InvoiceService invoiceService;
    private final OtpService otpService;
    private final LiveInvoiceDAO liveInvoiceDAO;

    @Autowired
    public ThreadService(
            @Lazy VarietyService varietyService,
            @Lazy ImportService importService,
            @Lazy RatingService ratingService,
            @Lazy ProductService productService,
            @Lazy CartService cartService,
            @Lazy OtpService otpService,
            @Lazy LiveInvoiceDAO liveInvoiceDAO,
            @Lazy EmailService emailService,
            @Lazy FileUploadService fileUploadService,
            @Lazy InvoiceService invoiceService,
            @Lazy ChatRoomService chatRoomService,
            @Lazy ViewAuditLogDAO viewAuditLogDAO) {
        this.varietyService = varietyService;
        this.importService = importService;
        this.ratingService = ratingService;
        this.productService = productService;
        this.cartService = cartService;
        this.fileUploadService = fileUploadService;
        this.chatRoomService = chatRoomService;
        this.otpService = otpService;
        this.emailService = emailService;
        this.invoiceService = invoiceService;
        this.viewAuditLogDAO = viewAuditLogDAO;
        this.liveInvoiceDAO = liveInvoiceDAO;
    }

    public void addVarietyByAttribute(Product product, List<VarietyAttribute> existingAttributes, VarietyAttribute newAttribute, boolean isSameMeasurement) {
        if (existingAttributes == null || existingAttributes.isEmpty()) {
            return;
        }

        for (VarietyAttribute varietyAttribute : existingAttributes) {
            try {
                Thread thread = new Thread(() -> processAddVarietyByAttribute(product, varietyAttribute, newAttribute, isSameMeasurement));
                thread.start();
            }
            catch (Exception e){
                log.trace(e.getMessage());
                log.trace("Product Id:" + product.getProductId());
                log.trace("Attribute 1:" + varietyAttribute.getAttributeId());
                log.trace("Attribute 2:" + newAttribute.getAttributeId());
            }
        }
    }

    public void addVarietyByAttribute(Product product, List<VarietyAttribute> colorAttributeList, List<VarietyAttribute> sizeAttributeList, boolean isSameMeasurement) {
        if (colorAttributeList == null || colorAttributeList.isEmpty()) {
            return;
        }

        for (VarietyAttribute colorAttribute : colorAttributeList) {
            Thread thread = new Thread(() -> addVarietyByAttribute(product, sizeAttributeList, colorAttribute, isSameMeasurement));
            thread.start();
        }
    }

    private void processAddVarietyByAttribute(Product product, VarietyAttribute existingAttribute, VarietyAttribute newAttribute, boolean isSameMeasurement) {
        Variety variety = new Variety();
        variety.addAttribute(newAttribute);
        variety.setMerchantId(product.getMerchantId());
        variety.setName(product.getName());
        variety.addAttribute(existingAttribute);
        variety.setProductId(product.getProductId());
        variety.setStatus(EnumStatus.ACTIVE.getValue());
        variety.setPrice(product.getPrice());
        variety.setStockAmount(0);

        if (isSameMeasurement) {
            variety.setWidth(product.getWidth());
            variety.setHeight(product.getHeight());
            variety.setLength(product.getLength());
            variety.setWeight(product.getWeight());
        }

        varietyService.addOrUpdateVariety(variety);
    }

    public void addImportItemToInvoice(ImportInvoice invoice, List<AddOrUpdateImportItemDTO> itemDTOList) {
        for (AddOrUpdateImportItemDTO dto : itemDTOList) {
            Thread thread = new Thread(() -> {
                try {
                    processAddOrUpdateImportItem(invoice, dto);
                } catch (FriendlyException e) {
                    log.trace(e.getMessage());
                    log.trace("Product ID:" + invoice.getImportInvoiceId());
                    log.trace("Variety ID:" + dto.getVarietyId());
                }
            });
            thread.start();
        }
    }

    public void processAddOrUpdateImportItem(ImportInvoice invoice, AddOrUpdateImportItemDTO dto) throws FriendlyException{
        importService.addOrUpdateImportItem(invoice, dto);
    }

    public void retrieveExternalInfoForProductDTO(ProductDTO dto, String viewerId){
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Future<List<Variety>> varietyFuture = executorService.submit(() ->
                varietyService.findByProductId(dto.getProductId()));

        Future<List<VarietyAttribute>> attributeFuture = executorService.submit(() ->
                productService.getAllExistedAttributeByProductId(dto.getProductId(), null));

        Future<List<RatingResultDTO>> ratingFuture = executorService.submit(() ->
                ratingService.getRatingByProductId(dto.getProductId()));

        Future<String> auditId = executorService.submit(() -> viewAuditLogDAO.saveAndFLushAudit(dto.getProductId(), viewerId, EnumObjectType.PRODUCT));

        executorService.shutdown();

        try {
            List<Variety> varietyList = varietyFuture.get();
            List<VarietyDTO> varietyDTOList = varietyService.transformVarietyToDTOList(varietyList);
            dto.setVarieties(varietyDTOList);

            List<VarietyAttribute> varietyAttributeList = attributeFuture.get();
            dto.setVarietyAttributeList(varietyAttributeList);

            List<RatingResultDTO> ratingList = ratingFuture.get();
            dto.setRatingList(ratingList);

            log.trace(auditId.get());
        } catch (InterruptedException | ExecutionException e) {
            log.trace(e.getMessage());
            e.printStackTrace();
        }
    }

    public void createNeededInfoForNewUser(User user, boolean isGoogle) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.submit(() -> cartService.createCartForNewUser(user.getUserId()));
        executorService.submit(() -> chatRoomService.createChatRoomForNewUser(user.getUserId()));
        if (!isGoogle) {
            executorService.submit(() -> {
                try {
                    otpService.sendOtpConfirmEmail(user.getUserEmail());
                } catch (FriendlyException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executorService.shutdown();
    }

    public void sendReceiptEmail(Invoice invoice) {
        List<InvoiceItemDTO> invoiceItemList = invoiceService.getInvoiceDetail(invoice.getInvoiceId());
        try{
            emailService.sendInvoiceEmail(invoiceItemList, invoice);
        } catch (Exception e){
            log.error("sending mail: {}", e.getMessage());
        }
    }

    private List<String> uploadMerchantRelatedDocuments(String merchantId, final List<MultipartFile> images){
        List<String> imagesUrl = new ArrayList<>();
        images.forEach(image -> imagesUrl.add(fileUploadService.uploadFile(image, "merchant/", merchantId)));

        return imagesUrl;
    }
    public String uploadMerchantAvatar(String merchantId, MultipartFile avatar){
        return fileUploadService.uploadFile(avatar, "merchant_avatar/", merchantId);
    }

    public String uploadMerchantCover(String merchantId, MultipartFile avatar){
        return fileUploadService.uploadFile(avatar, "merchant_cover/", merchantId);
    }

    public void uploadMerchantFiles(Merchant merchant, List<MultipartFile> relatedDocuments, MultipartFile coverFile, MultipartFile avatarFile) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        Future<List<String>> relatedDocumentsFuture = executorService.submit(() -> uploadMerchantRelatedDocuments(merchant.getMerchantId(), relatedDocuments));
        Future<String> coverFileFuture = executorService.submit(() -> uploadMerchantCover(merchant.getMerchantId(), coverFile));
        Future<String> avatarFileFuture = executorService.submit(() -> uploadMerchantAvatar(merchant.getMerchantId(), avatarFile));

        executorService.shutdown();

        try {
            List<String> relatedDocumentsUrl = relatedDocumentsFuture.get();
            if (relatedDocumentsUrl != null && !relatedDocumentsUrl.isEmpty()){
                merchant.setRelatedDocuments(relatedDocumentsUrl);
            }
            merchant.setMerchantCover(coverFileFuture.get());
            merchant.setMerchantAvatar(avatarFileFuture.get());

        } catch (InterruptedException | ExecutionException e) {
            log.trace(e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendReceiptEmail(LiveInvoice invoice) throws FriendlyException {
        List<LiveInvoiceItem> liveItems = liveInvoiceDAO.findItemsByLiveInvoiceId(invoice.getLiveInvoiceId());

        List<InvoiceItemDTO> liveItemDTOList = liveItems.stream().map(liveInvoiceItem -> {
            LiveItem item = liveInvoiceItem.getLiveItem();
            return InvoiceItemDTO.builder()
                    .name(item.getName())
                    .unitPrice(item.getLivePrice())
                    .quantity(liveInvoiceItem.getQuantity())
                    .totalPrice(liveInvoiceItem.getTotalPrice())
                    .build();
        }).toList();

        emailService.sendInvoiceEmail(liveItemDTOList, Invoice.builder()
                        .invoiceId(invoice.getLiveInvoiceId())
                        .userEmail(invoice.getUserEmail())
                        .recipientName(invoice.getRecipientName())
                        .phoneNumber(invoice.getPhoneNumber())
                        .cityName(invoice.getCityName())
                        .districtName(invoice.getDistrictName())
                        .wardName(invoice.getWardName())
                        .streetName(invoice.getStreetName())
                        .totalPrice(invoice.getTotalPrice())
                        .finalPrice(invoice.getFinalPrice())
                        .shippingFee(invoice.getShippingFee())
                        .discountPrice(invoice.getDiscountPrice())
                        .createdDate(invoice.getCreatedDate())
                        .status(invoice.getStatus())
                .build());
    }
}
