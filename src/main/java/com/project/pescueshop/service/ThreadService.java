package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.*;
import com.project.pescueshop.model.entity.*;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.ViewAuditLogDAO;
import com.project.pescueshop.util.constant.EnumObjectType;
import com.project.pescueshop.util.constant.EnumStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

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
    private final PaymentService paymentService;
    private final EmailService emailService;
    private final InvoiceService invoiceService;
    private final OtpService otpService;

    @Autowired
    public ThreadService(
            @Lazy VarietyService varietyService,
            @Lazy ImportService importService,
            @Lazy RatingService ratingService,
            @Lazy ProductService productService,
            @Lazy CartService cartService,
            @Lazy OtpService otpService,
            @Lazy PaymentService paymentService,
            @Lazy EmailService emailService,
            @Lazy InvoiceService invoiceService,
            @Lazy ChatRoomService chatRoomService,
            @Lazy ViewAuditLogDAO viewAuditLogDAO) {
        this.varietyService = varietyService;
        this.importService = importService;
        this.ratingService = ratingService;
        this.productService = productService;
        this.cartService = cartService;
        this.chatRoomService = chatRoomService;
        this.otpService = otpService;
        this.paymentService = paymentService;
        this.emailService = emailService;
        this.invoiceService = invoiceService;
        this.viewAuditLogDAO = viewAuditLogDAO;
    }

    public void addVarietyByAttribute(Product product, List<VarietyAttribute> existingAttributes, VarietyAttribute newAttribute) {
        if (existingAttributes == null || existingAttributes.isEmpty()) {
            return;
        }

        for (VarietyAttribute varietyAttribute : existingAttributes) {
            try {
                Thread thread = new Thread(() -> processAddVarietyByAttribute(product, varietyAttribute, newAttribute));
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

    public void addVarietyByAttribute(Product product, List<VarietyAttribute> colorAttributeList, List<VarietyAttribute> sizeAttributeList) {
        if (colorAttributeList == null || colorAttributeList.isEmpty()) {
            return;
        }

        for (VarietyAttribute colorAttribute : colorAttributeList) {
            Thread thread = new Thread(() -> addVarietyByAttribute(product, sizeAttributeList, colorAttribute));
            thread.start();
        }
    }

    private void processAddVarietyByAttribute(Product product, VarietyAttribute existingAttribute, VarietyAttribute newAttribute) {
        Variety variety = new Variety();
        variety.addAttribute(newAttribute);
        variety.setName(product.getName());
        variety.addAttribute(existingAttribute);
        variety.setProductId(product.getProductId());
        variety.setStatus(EnumStatus.ACTIVE.getValue());
        variety.setPrice(product.getPrice());
        variety.setStockAmount(0);
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

    public void retrieveExternalInfoForProductDTO(ProductDTO dto){
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Future<List<Variety>> varietyFuture = executorService.submit(() ->
                varietyService.findByProductId(dto.getProductId()));

        Future<List<VarietyAttribute>> attributeFuture = executorService.submit(() ->
                productService.getAllExistedAttributeByProductId(dto.getProductId(), null));

        Future<List<RatingResultDTO>> ratingFuture = executorService.submit(() ->
                ratingService.getRatingByProductId(dto.getProductId()));

        Future<String> auditId = executorService.submit(() -> viewAuditLogDAO.saveAndFLushAudit(dto.getProductId(), EnumObjectType.PRODUCT));

        executorService.shutdown();

        try {
            List<Variety> varietyList = varietyFuture.get();
            List<VarietyDTO> varietyDTOList = varietyService.transformVarietyToDTOList(varietyList);
            dto.setVarieties(varietyDTOList);

            List<VarietyAttribute> varietyAttributeList = attributeFuture.get();
            dto.setVarietyAttributeList(varietyAttributeList);

            List<RatingResultDTO> ratingList = ratingFuture.get();
            dto.setRatingList(ratingList);

            System.out.println(auditId.get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
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
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<List<InvoiceItemDTO>> invoiceItemListFuture = executorService.submit(() -> invoiceService.getInvoiceDetail(invoice.getInvoiceId()));

        try {
            List<InvoiceItemDTO> invoiceItemList = invoiceItemListFuture.get();

            executorService.submit(() -> {
                try {
                    emailService.sendInvoiceEmail(invoiceItemList, invoice);
                } catch (FriendlyException e) {
                    log.trace(e.getMessage());
                    throw new RuntimeException(e);
                }
            });
        }
        catch (Exception e){
            log.trace(e.getMessage());
        }
    }
}
