package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "message", pluralNoun = "messageList")
public class MessageDTO {
    private String senderId;
    private String recipientId;
    private String content;
}
