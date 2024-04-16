package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "product", pluralNoun = "productList")
public class UpdateVarietyMeasurementRequest {
    private String varietyId;
    private Double width;
    private Double height;
    private Double length;
    private Double weight;
}
