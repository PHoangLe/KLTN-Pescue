package com.project.pescueshop.repository.mapper;

import com.project.pescueshop.model.dto.ImportItemGroupDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ImportItemGroupListMapper implements RowMapper<ImportItemGroupDTO> {
    @Override
    public ImportItemGroupDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ImportItemGroupDTO.builder()
                .productId(rs.getString("product_id"))
                .importInvoiceId(rs.getString("import_invoice_id"))
                .productName(rs.getString("product_name"))
                .productImage(rs.getString("image"))
                .totalImport(rs.getLong("total_import"))
                .build();
    }
}
