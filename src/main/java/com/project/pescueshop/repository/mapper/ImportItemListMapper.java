package com.project.pescueshop.repository.mapper;

import com.project.pescueshop.model.dto.ImportItemListDTO;
import com.project.pescueshop.util.Util;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ImportItemListMapper implements RowMapper<ImportItemListDTO> {
    @Override
    public ImportItemListDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ImportItemListDTO dto = ImportItemListDTO.builder()
                .varietyId(rs.getString("variety_id"))
                .name(rs.getString("name"))
                .importPrice(rs.getLong("import_price"))
                .quantity(rs.getInt("quantity"))
                .totalImportPrice(rs.getLong("total_import_price"))
                .image(rs.getString("image"))
                .build();

        List<String> attributeName = Util.getListStringFromString(rs.getString("attribute_name"));
        dto.setAttributeName(attributeName);

        return dto;
    }
}
