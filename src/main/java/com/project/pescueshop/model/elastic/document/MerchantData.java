package com.project.pescueshop.model.elastic.document;

import com.project.pescueshop.model.entity.Merchant;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
public class MerchantData {
    private String merchantId;
    private String userId;
    private String merchantName;
    private String merchantDescription;
    private Date createdDate;
    private String merchantAvatar;
    private String merchantCover;
    private Integer rating;
    private String cityName;
    private String districtName;
    private String wardName;
    private String cityCode;
    private Integer districtId;
    private String wardCode;
    private String phoneNumber;
    private Integer noProduct;
    private Boolean isLiveable;
    private Boolean isSuspended;
    private Boolean isApproved;

    public static MerchantData fromMerchant(Merchant merchant){
        return MerchantData.builder()
                .merchantId(merchant.getMerchantId())
                .userId(merchant.getUserId())
                .merchantName(merchant.getMerchantName())
                .merchantDescription(merchant.getMerchantDescription())
                .createdDate(merchant.getCreatedDate())
                .merchantAvatar(merchant.getMerchantAvatar())
                .merchantCover(merchant.getMerchantCover())
                .rating(merchant.getRating())
                .cityName(merchant.getCityName())
                .districtName(merchant.getDistrictName())
                .wardName(merchant.getWardName())
                .cityCode(merchant.getCityCode())
                .districtId(merchant.getDistrictId())
                .wardCode(merchant.getWardCode())
                .phoneNumber(merchant.getPhoneNumber())
                .noProduct(merchant.getNoProduct())
                .isLiveable(merchant.getIsLiveable())
                .isSuspended(merchant.getIsSuspended())
                .isApproved(merchant.getIsApproved())
                .build();
    }
}
