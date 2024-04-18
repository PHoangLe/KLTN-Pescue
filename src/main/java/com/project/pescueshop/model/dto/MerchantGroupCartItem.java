package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "merchantCartGroup", pluralNoun = "merchantCartGroupList")
public class MerchantGroupCartItem {
    private String merchantId;
    private String merchantName;
    private String merchantAvatar;
    private String merchantLocation;
    List<CartItemDTO> cartItemDTOList;
}
