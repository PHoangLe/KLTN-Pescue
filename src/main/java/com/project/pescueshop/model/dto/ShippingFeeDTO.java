package com.project.pescueshop.model.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class ShippingFeeDTO {
    private String merchantId;
    private Long shippingFee;
}
