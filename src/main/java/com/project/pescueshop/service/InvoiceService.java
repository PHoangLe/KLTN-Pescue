package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.InvoiceItemDTO;
import com.project.pescueshop.model.dto.InvoiceListResultDTO;
import com.project.pescueshop.model.dto.InvoicesListDTO;
import com.project.pescueshop.model.entity.Invoice;
import com.project.pescueshop.model.entity.InvoiceItem;
import com.project.pescueshop.model.entity.Merchant;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.entity.live.LiveInvoice;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.LiveInvoiceDAO;
import com.project.pescueshop.repository.dao.PaymentDAO;
import com.project.pescueshop.util.constant.EnumInvoiceStatus;
import com.project.pescueshop.util.constant.EnumResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class InvoiceService {
    private final PaymentDAO paymentDAO;
    private final LiveInvoiceDAO liveInvoiceDAO;
    private final AuthenticationService authenticationService;

    public List<InvoiceListResultDTO> findAllInvoice(Date fromDate, Date toDate) throws FriendlyException {
        List<InvoiceListResultDTO> resp = paymentDAO.findAllInvoice().stream()
                .filter(invoice -> invoice.getCreatedDate().before(toDate) && invoice.getCreatedDate().after(fromDate))
                .toList();

        if (!AuthenticationService.isCurrentAdmin()){
            Merchant merchant = authenticationService.getCurrentMerchant();
            return resp.stream().filter(invoice -> invoice.getMerchantId().equals(merchant.getMerchantId())).toList();
        }

        return resp;
    }

    public List<InvoiceItemDTO> getInvoiceDetail(String invoiceId){
        return paymentDAO.getInvoiceDetail(invoiceId);
    }

    public Invoice updateInvoiceStatus(Invoice invoice,EnumInvoiceStatus status){
        invoice.setStatus(status.getValue());
        paymentDAO.saveAndFlushInvoice(invoice);
        return invoice;
    }

    public Invoice updateInvoiceStatus(String invoiceId, String status) throws FriendlyException {
        EnumInvoiceStatus enumInvoiceStatus = EnumInvoiceStatus.getByValue(status);
        Invoice invoice = paymentDAO.findInvoiceById(invoiceId);

        if (invoice == null){
            throw new FriendlyException(EnumResponseCode.INVOICE_NOT_FOUND);
        }

        return updateInvoiceStatus(invoice, enumInvoiceStatus);
    }

    public List<Invoice> getInvoiceInfoByUser(User user) {
        return paymentDAO.findAllInvoiceByUserId(user.getUserId());
    }

    public Map<String, List<InvoiceItemDTO>> getAllInvoicesGroupedByMerchantInCart(String cartId) {
        List<InvoiceItemDTO> invoiceItemDTOS = paymentDAO.getAllInvoiceItemsGroupedByMerchantInCart(cartId);

        return invoiceItemDTOS.stream().
                collect(Collectors.groupingBy(InvoiceItemDTO::getMerchantId));
    }


    public void addInvoiceItemsToInvoice(Invoice invoice, List<InvoiceItemDTO> invoiceItemDTO) {
        invoiceItemDTO.forEach(item -> {
            InvoiceItem invoiceItem = InvoiceItem.builder()
                    .invoiceId(invoice.getInvoiceId())
                    .merchantId(item.getMerchantId())
                    .varietyId(item.getVarietyId())
                    .quantity(item.getQuantity())
                    .totalPrice(item.getTotalPrice())
                    .build();

            saveAndFlushInvoiceItem(invoiceItem);
        });
    }

    private void saveAndFlushInvoiceItem(InvoiceItem invoiceItem) {
        paymentDAO.saveAndFlushItem(invoiceItem);
    }

    public InvoicesListDTO getAllInvoiceInfoByUser(User user) {
        List<Invoice> invoices = getInvoiceInfoByUser(user);
        List<LiveInvoice> liveInvoices = liveInvoiceDAO.findAllLiveInvoiceByUserId(user.getUserId());

        return InvoicesListDTO.builder()
                .invoices(invoices)
                .liveInvoices(liveInvoices)
                .build();
    }

    public List<InvoiceItemDTO> getLiveInvoiceDetail(String liveInvoiceId) {
        return liveInvoiceDAO.getLiveInvoiceDetail(liveInvoiceId);
    }
}
