package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.dto.InvoiceItemDTO;
import com.project.pescueshop.model.dto.InvoiceListResultDTO;
import com.project.pescueshop.model.entity.*;
import com.project.pescueshop.repository.jpa.InvoiceItemRepository;
import com.project.pescueshop.repository.jpa.InvoiceRepository;
import com.project.pescueshop.repository.mapper.InvoiceItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PaymentDAO extends BaseDAO{
    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceItemMapper invoiceItemMapper;

    public void saveAndFlushInvoice(Invoice invoice){
        invoiceRepository.saveAndFlush(invoice);
    }

    public void saveAndFlushItem(InvoiceItem item){
        invoiceItemRepository.saveAndFlush(item);
    }

    public Invoice findInvoiceById(String invoiceId){
        return invoiceRepository.findById(invoiceId).orElse(null);
    }

    public List<InvoiceItemDTO> getInvoiceDetail(String invoiceId){
        String sql = "SELECT * FROM get_invoice_details(:p_invoice_id);";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_invoice_id", invoiceId);

        return jdbcTemplate.query(sql, parameters, invoiceItemMapper);
    }

    public List<InvoiceListResultDTO> findAllInvoice(){
        List<Object[]> invoices = invoiceRepository.findAllInvoice();

        List<InvoiceListResultDTO> results = new ArrayList<>();

        for (Object[] object : invoices){
            results.add(new InvoiceListResultDTO((Invoice) object[0], object[1] + " " + object[2]));
        }

        return results;
    }

    public List<Invoice> findAllInvoiceByUserId(String userId) {
        return invoiceRepository.findAllInvoiceByUserId(userId);
    }

    public List<InvoiceItem> findInvoiceItemByInvoiceId(String invoiceId) {
        return invoiceItemRepository.findInvoiceItemByInvoiceId(invoiceId);
    }

    public List<InvoiceItemDTO> getAllInvoiceItemsGroupedByMerchantInCart(String cartId) {
        String sql = "SELECT * FROM get_selected_invoice_items(:p_cart_id);";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_cart_id", cartId);

        return jdbcTemplate.query(sql, parameters, invoiceItemMapper);
    }
}
