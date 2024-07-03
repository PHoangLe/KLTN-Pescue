package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.dto.InvoiceItemDTO;
import com.project.pescueshop.model.dto.InvoiceListResultDTO;
import com.project.pescueshop.model.entity.*;
import com.project.pescueshop.repository.jpa.InvoiceItemRepository;
import com.project.pescueshop.repository.jpa.InvoiceRepository;
import com.project.pescueshop.repository.mapper.InvoiceItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        String sql = "SELECT i.user_id, m.user_id as merchant_user_id, i.invoice_id, ii.quantity, " +
                "           ii.total_price as total_price, v.variety_id, v.name, v.merchant_id, v.product_id, v.price as unit_price, " +
                "           (SELECT pi.images FROM product_images pi WHERE v.product_id = pi.product_product_id LIMIT 1) as image, v.stock_amount " +
                "            , v.weight, v.width, v.length, v.height " +
                "    FROM invoice i " +
                "    JOIN invoice_item ii ON i.invoice_id = ii.invoice_id" +
                "    JOIN merchant m ON i.merchant_id = m.merchant_id " +
                "    JOIN variety v ON ii.variety_id = v.variety_id " +
                "    WHERE i.invoice_id = :p_invoice_id;";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_invoice_id", invoiceId);

        return jdbcTemplate.query(sql, parameters, invoiceItemMapper);
    }

    public Page<Invoice> findAllInvoice(Date fromDate, Date toDate, Pageable pageable, String merchantId){
        return invoiceRepository.findAllInvoice(fromDate, toDate, pageable, merchantId);
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
