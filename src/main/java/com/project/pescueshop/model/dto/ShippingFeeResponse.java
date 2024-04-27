package com.project.pescueshop.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingFeeResponse {
    private Integer code;
    private String message;
    @JsonProperty("data")
    private Data data;

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        private Long total;
        @JsonProperty("service_fee")
        private Long serviceFee;
        @JsonProperty("insurance_fee")
        private Long insuranceFee;
        @JsonProperty("pick_station_fee")
        private Long pickStationFee;
        @JsonProperty("coupon_value")
        private Long couponValue;
        @JsonProperty("r2s_fee")
        private Long r2sFee;
        @JsonProperty("document_return")
        private Long documentReturn;
        @JsonProperty("double_check")
        private Long doubleCheck;
        @JsonProperty("cod_fee")
        private Long codFee;
        @JsonProperty("pick_remote_areas_fee")
        private Long pickRemoteAreasFee;
        @JsonProperty("deliver_remote_areas_fee")
        private Long deliverRemoteAreasFee;
        @JsonProperty("cod_failed_fee")
        private Long codFailedFee;
    }
}