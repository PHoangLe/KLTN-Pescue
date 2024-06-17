package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "request", pluralNoun = "requests")
public class UpdateVarietyStockAmountRequest {
    private String varietyId;
    private int stockAmount;
}
