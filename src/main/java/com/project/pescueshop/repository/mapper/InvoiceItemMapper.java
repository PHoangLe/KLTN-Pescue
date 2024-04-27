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
        return InvoiceItemDTO.builder()
                .userId(rs.getString("user_id"))
                .invoiceId(rs.getString("invoice_id"))
                .quantity(rs.getInt("quantity"))
                .totalPrice(rs.getLong("total_price"))
                .varietyId(rs.getString("variety_id"))
                .name(rs.getString("name"))
                .merchantId(rs.getString("merchant_id"))
                .productId(rs.getString("product_id"))
                .unitPrice(rs.getLong("unit_price"))
                .image(rs.getString("image"))
                .stockAmount(rs.getInt("stock_amount"))
                .weight(rs.getInt("weight"))
                .height(rs.getInt("height"))
                .length(rs.getInt("length"))
                .width(rs.getInt("width"))
                .build();
    }
}
