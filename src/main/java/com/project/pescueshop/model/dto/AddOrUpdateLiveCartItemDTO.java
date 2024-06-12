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
public class AddOrUpdateLiveCartItemDTO {
    private String liveItemId;
    private Integer quantity;
    private Boolean isSelected;
}
