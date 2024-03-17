package com.project.pescueshop.repository.mapper;

import com.project.pescueshop.model.dto.GlobalSearchResultDTO;
import com.project.pescueshop.model.dto.ImportItemGroupDTO;
import com.project.pescueshop.model.dto.ImportItemListDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GlobalSearchResultMapper implements RowMapper<GlobalSearchResultDTO> {
    @Override
    public GlobalSearchResultDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return GlobalSearchResultDTO.builder()
                .groupName(rs.getString("group_name"))
                .itemId(rs.getString("item_id"))
                .itemName(rs.getString("item_name"))
                .itemImage(rs.getString("item_image"))
                .build();
    }
}
