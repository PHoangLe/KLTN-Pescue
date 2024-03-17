package com.project.pescueshop.model.entity;

import com.project.pescueshop.model.annotation.Name;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "CHAT_MESSAGE")
@Entity
@Builder
@AllArgsConstructor
@Name(prefix = "CHME", noun = "chatMessage", pluralNoun = "chatMessageList")
public class ChatMessage {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.project.pescueshop.util.CustomIdGenerator")
    private String messageId;
    private String chatRoomId;
    private String senderId;
    private String recipientId;
    private String content;
    private Date timestamp;
    private String status;
}
