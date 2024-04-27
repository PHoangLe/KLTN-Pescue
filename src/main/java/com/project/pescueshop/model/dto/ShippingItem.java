package com.project.pescueshop.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.pescueshop.model.annotation.Name;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "shippingItem", pluralNoun = "shippingItemList")
public class ShippingItem {
    @JsonProperty("item_id")
    private String itemId;
    @JsonProperty("item_name")
    private String itemName;
    private Integer quantity;
    private Integer weight;
    private Integer height;
    private Integer width;
    private Integer length;
}
