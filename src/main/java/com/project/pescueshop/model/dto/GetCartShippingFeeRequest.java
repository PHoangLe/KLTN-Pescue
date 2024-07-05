package com.project.pescueshop.model.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class GetCartShippingFeeRequest {
    private String cartId;

    private String wardCode;
    private Integer districtId;
    private String cityCode;
}
