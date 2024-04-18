package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Name(noun = "item", pluralNoun = "items")
public class CartItemDTO {
    private String cartItemId;
    private String userId;
    private String cartId;
    private String varietyId;
    private String name;
    private Long unitPrice;
    private Integer quantity;
    private Long totalItemPrice;
    private Boolean isSelected;
    private String image;
    private Integer stockAmount;
    private List<String> listAttributeName;
    private String merchantId;
    private String merchantName;
    private String merchantAvatar;
    private String cityName;
    private String districtName;
    private String wardName;
}
