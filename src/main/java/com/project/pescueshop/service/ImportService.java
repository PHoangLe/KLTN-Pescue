package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.AddOrUpdateImportItemDTO;
import com.project.pescueshop.model.dto.ImportItemGroupDTO;
import com.project.pescueshop.model.dto.ImportItemListDTO;
import com.project.pescueshop.model.entity.*;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.ImportDAO;
import com.project.pescueshop.util.constant.EnumResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class ImportService {
    private final ImportDAO importDAO;
    private final VarietyService varietyService;
    private final ThreadService threadService;

    public ImportInvoice addNewImportInvoice(User user, List<AddOrUpdateImportItemDTO> itemDTOList){
        ImportInvoice invoice = ImportInvoice.builder()
                .createdDate(new Date())
                .userId(user.getUserId())
                .build();

        List<AddOrUpdateImportItemDTO> filteredList = itemDTOList.stream()
                .filter(dto -> dto.getQuantity() != 0)
                .toList();

        Long totalPrice = filteredList.stream()
                        .mapToLong(item -> item.getImportPrice() * item.getQuantity())
                        .sum();

        invoice.setTotalPrice(totalPrice);

        importDAO.saveAndFlushInvoice(invoice);

        CompletableFuture.runAsync(() -> {
            threadService.addImportItemToInvoice(invoice, filteredList);
        });

        return invoice;
    }

    public ImportInvoice getImportInvoiceById(String importInvoiceId){
        return importDAO.findImportInvoiceById(importInvoiceId);
    }

    public List<ImportInvoice> getAllImportInvoice(Date fromDate, Date toDate){
        if (fromDate == null || toDate == null){
            return importDAO.findAllImportInvoice();
        }

        return importDAO.findAllImportInvoice().stream()
                .filter(invoice -> invoice.getCreatedDate().after(fromDate) && invoice.getCreatedDate().before(toDate))
                .toList();
    }

    public void addOrUpdateImportItem(ImportInvoice invoice, AddOrUpdateImportItemDTO dto) throws FriendlyException {
        Variety variety = varietyService.findById(dto.getVarietyId());

        if (variety == null){
            throw new FriendlyException(EnumResponseCode.VARIETY_NOT_FOUND);
        }

        ImportItem importItem = importDAO.findImportItemByVarietyIdAndInvoiceId(dto.getVarietyId(), dto.getImportInvoiceId());

        if (invoice == null){
            throw new FriendlyException(EnumResponseCode.IMPORT_INVOICE_NOT_FOUND);
        }

        if (importItem != null && dto.getQuantity() == 0){
            importDAO.deleteImportItem(importItem);
            return;
        }

        if (importItem == null){
            importItem = new ImportItem();
        }
        importItem.setImportInvoiceId(invoice.getImportInvoiceId());
        importItem.setVariety(variety);
        importItem.setVarietyId(variety.getVarietyId());
        importItem.setQuantity(dto.getQuantity());
        importItem.setImportPrice(dto.getImportPrice());
        importItem.setTotalImportPrice(dto.getQuantity() * dto.getImportPrice());

        importDAO.saveAndFlushItem(importItem);
    }

    public List<ImportItemListDTO> getImportItemListByInvoiceId(String invoiceId, String productId){
        return importDAO.getImportItemsByInvoiceId(invoiceId, productId);
    }

    public List<ImportItemGroupDTO> getImportItemGroupsByInvoiceId(String invoiceId){
        return importDAO.getImportItemGroupsByInvoiceId(invoiceId);
    }
}
