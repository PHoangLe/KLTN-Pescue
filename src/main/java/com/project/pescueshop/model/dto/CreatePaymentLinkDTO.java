package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;
import org.springframework.messaging.simp.stomp.StompReactorNettyCodec;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "payment", pluralNoun = "payment")
public class CreatePaymentLinkDTO {
    private String content;
    private String returnUrl;
    private Long value;
}
