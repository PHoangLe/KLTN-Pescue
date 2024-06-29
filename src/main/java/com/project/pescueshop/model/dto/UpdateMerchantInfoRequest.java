package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "liveItemCheckOutInfo")
public class UpdateMerchantInfoRequest {
    private String merchantId;
    private String merchantName;
    private String merchantDescription;
    private String cityName;
    private String districtName;
    private String wardName;
    private String cityCode;
    private Integer districtId;
    private String wardCode;
    private String phoneNumber;
}
