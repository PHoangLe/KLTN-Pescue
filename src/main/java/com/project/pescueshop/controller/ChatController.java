package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.MessageDTO;
import com.project.pescueshop.model.entity.ChatMessage;
import com.project.pescueshop.service.ChatMessageService;
import com.project.pescueshop.service.ChatRoomService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@Api
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/private-message")
    public ChatMessage processMessage(@Payload MessageDTO messageDTO) {
        log.trace(messageDTO.getContent());
        Optional<String> chatID = chatRoomService
                .getChatId(messageDTO.getSenderId(), messageDTO.getRecipientId(), true);

        if (chatID.isEmpty())
            return null;

        ChatMessage chatMessage = chatMessageService.save(messageDTO, chatID.get());

        if (chatMessage == null)
            return null;

        messagingTemplate.convertAndSendToUser(chatMessage.getRecipientId(), "/private", chatMessage); // /user/userID/private
        log.trace("Sent message from: " + chatMessage.getSenderId() + " to: " + chatMessage.getRecipientId());
        return chatMessage;
    }

    @GetMapping("/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Long> countNewMessages(@PathVariable String recipientId, @PathVariable String senderId) {
        return ResponseEntity.ok(chatMessageService.countNewMessage(senderId, recipientId));
    }

    @GetMapping("/getAllChatRoomByUserID/{userID}")
    public ResponseEntity<Object> getAllChatRoomByUserID(@PathVariable String userID) {
        return ResponseEntity
                .ok(chatRoomService.findAllChatRoomByUserID(userID));
    }

    @GetMapping("/getAllMessageBySenderIDAndRecipientID/{chatRoomID}/{senderID}/{recipientID}")
    public ResponseEntity<Object> getAllMessageBySenderIDAndRecipientID(@PathVariable String recipientID, @PathVariable String senderID, @PathVariable String chatRoomID) {
        List<ChatMessage> allChatMessageByChatRoomID = chatRoomService.findAllChatMessageByChatRoomID(chatRoomID, senderID, recipientID);
        return ResponseEntity
                .ok(allChatMessageByChatRoomID);
    }

    @PutMapping("/seenMessage/{senderID}/{recipientID}")
    public ResponseEntity<Object> seenMessage(@PathVariable String recipientID, @PathVariable String senderID) {
        try {
            chatMessageService.seenUnreadMessages(recipientID, senderID);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }
        return ResponseEntity.ok("");
    }
}