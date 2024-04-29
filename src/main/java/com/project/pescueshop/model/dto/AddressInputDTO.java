package com.project.pescueshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressInputDTO {
    private String streetName;
    private String wardName;
    private String wardCode;
    private String districtName;
    private Integer districtId;
    private String cityName;
}
