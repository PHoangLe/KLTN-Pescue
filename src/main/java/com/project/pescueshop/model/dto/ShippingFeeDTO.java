package com.project.pescueshop.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ShippingFeeDTO {
    private String merchantId;
    private Long shippingFee;
}
