package com.project.pescueshop.repository.mapper;

import com.project.pescueshop.model.dto.CartItemDTO;
import com.project.pescueshop.util.Util;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class CartItemMapper implements RowMapper<CartItemDTO> {
    @Override
    public CartItemDTO mapRow(ResultSet rs, int rowNum) throws SQLException {

        CartItemDTO cartItemDTO = CartItemDTO.builder()
                .userId(rs.getString("user_id"))
                .cartId(rs.getString("cart_id"))
                .cartItemId(rs.getString("cart_item_id"))
                .isSelected(rs.getBoolean("is_selected"))
                .quantity(rs.getInt("quantity"))
                .totalItemPrice(rs.getLong("total_item_price"))
                .varietyId(rs.getString("variety_id"))
                .name(rs.getString("name"))
                .unitPrice(rs.getLong("unit_price"))
                .image(rs.getString("image"))
                .stockAmount(rs.getInt("stock_amount"))
                .build();

        List<String> attributeNameList = Util.getListStringFromString(rs.getString("list_attribute_name"));
        cartItemDTO.setListAttributeName(attributeNameList);

        List<String> merchantInfo = Util.getListStringFromString(rs.getString("merchant_info"));
        cartItemDTO.setMerchantId(merchantInfo.get(0));
        cartItemDTO.setMerchantName(merchantInfo.get(1));
        cartItemDTO.setMerchantAvatar(merchantInfo.get(2));
        cartItemDTO.setCityName(merchantInfo.get(3));
        cartItemDTO.setDistrictName(merchantInfo.get(4));
        cartItemDTO.setWardName(merchantInfo.get(5));

        return cartItemDTO;
    }
}
