package com.project.pescueshop.repository.mapper;

import com.project.pescueshop.model.dto.InvoiceDataDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class InvoiceDataMapper implements RowMapper<InvoiceDataDTO> {
    @Override
    public InvoiceDataDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return InvoiceDataDTO.builder()
                .userId(rs.getString("user_id"))
                .invoiceId(rs.getString("invoice_id"))
                .productId(rs.getString("product_id"))
                .build();
    }
}
