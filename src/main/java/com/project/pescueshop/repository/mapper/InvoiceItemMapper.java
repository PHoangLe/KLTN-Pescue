package com.project.pescueshop.repository.mapper;

import com.project.pescueshop.model.dto.InvoiceItemDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class InvoiceItemMapper implements RowMapper<InvoiceItemDTO> {
    @Override
    public InvoiceItemDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        InvoiceItemDTO dto = InvoiceItemDTO.builder()
                .userId(rs.getString("user_id"))
                .invoiceId(rs.getString("invoice_id"))
                .quantity(rs.getInt("quantity"))
                .total_price(rs.getLong("total_price"))
                .varietyId(rs.getString("variety_id"))
                .name(rs.getString("name"))
                .productId(rs.getString("product_id"))
                .unitPrice(rs.getLong("unit_price"))
                .image(rs.getString("image"))
                .stock_amount(rs.getInt("stock_amount"))
                .build();


        return dto;
    }
}
