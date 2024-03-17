package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "paymentInput", pluralNoun = "paymentInput")
public class PaymentInputDTO {
    private String invoiceId;
    private String returnUrl;
}
