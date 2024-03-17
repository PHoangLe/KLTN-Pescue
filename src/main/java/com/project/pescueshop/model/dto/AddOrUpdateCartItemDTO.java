package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "cartItem", pluralNoun = "itemList")
public class AddOrUpdateCartItemDTO {
    private String varietyId;
    private Integer quantity;
    private Boolean isSelected;
}
