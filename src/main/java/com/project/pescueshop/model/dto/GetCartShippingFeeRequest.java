package com.project.pescueshop.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class GetCartShippingFeeRequest {
    private String cartId;

    private String wardCode;
    private Integer districtId;
    private String cityCode;
}
