package com.project.pescueshop.repository.mapper;

import com.project.pescueshop.model.dto.ProductListDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component

public class ListProductMapper implements RowMapper<ProductListDTO> {
    @Override
    public ProductListDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ProductListDTO.builder()
                .productId(rs.getString("product_id"))
                .name(rs.getString("name"))
                .price(rs.getLong("price"))
                .image(rs.getString("product_image"))
                .description(rs.getString("description"))
                .avgRating(rs.getInt("avg_rating"))
                .totalRecord(rs.getLong("total_products"))
                .brandName(rs.getString("brand_name"))
                .categoryName(rs.getString("category_name"))
                .build();
    }
}
