package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "checkoutResult", pluralNoun = "checkoutResult")@Data
@Builder
public class CheckoutResultDTO {
    private String paymentUrl;
    private String cartId;
    private List<String> invoiceIdList;
}
