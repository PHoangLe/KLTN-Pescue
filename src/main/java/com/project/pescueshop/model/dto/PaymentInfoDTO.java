package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import com.project.pescueshop.model.entity.Address;
import com.project.pescueshop.model.entity.Voucher;
import lombok.*;

import java.util.Map;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "paymentInfo", pluralNoun = "paymentInfoList")
public class PaymentInfoDTO {
    private Address address;
    private Map<String, Voucher> voucherByMerchantMap;
    private String phoneNumber;
    private String recipientName;
    private String paymentType;
    private String returnUrl;
    private Long shippingFee;
    private String userEmail;
}
