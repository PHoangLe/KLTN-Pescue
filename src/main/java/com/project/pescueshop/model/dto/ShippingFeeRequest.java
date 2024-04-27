package com.project.pescueshop.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.pescueshop.model.annotation.Name;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "shippingFeeRequest", pluralNoun = "shippingFeeRequestList")
public class ShippingFeeRequest {
    @JsonProperty("from_district_id")
    private Integer fromDistrictId;
    @JsonProperty("to_district_id")
    private Integer toDistrictId;
    @JsonProperty("from_ward_code")
    private String fromWardCode;
    @JsonProperty("to_ward_code")
    private String toWardCode;
    @JsonProperty(value = "service_type_id", defaultValue = "2")
    private Integer serviceTypeId;
    private Integer weight;
    private List<ShippingItem> items;
}
