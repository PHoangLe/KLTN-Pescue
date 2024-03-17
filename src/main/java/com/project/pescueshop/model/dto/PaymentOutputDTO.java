package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "paymentOutput", pluralNoun = "paymentOutput")
public class PaymentOutputDTO {
    private String status;
    private String message;
    private String url;
}