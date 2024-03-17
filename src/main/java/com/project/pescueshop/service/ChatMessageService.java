package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.MessageDTO;
import com.project.pescueshop.model.entity.ChatMessage;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.repository.dao.ChatDAO;
import com.project.pescueshop.util.constant.EnumMessageStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {
    private final ChatDAO chatDAO;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    public ChatMessage save(MessageDTO dto, String chatRoomID){
        ChatMessage message = new ChatMessage();
        User sender = userService.findById(dto.getSenderId());
        User recipient = userService.findById(dto.getRecipientId());

        if (sender == null || recipient == null)
            return null;

        message.setChatRoomId(chatRoomID);
        message.setSenderId(dto.getSenderId());
        message.setRecipientId(dto.getRecipientId());
        message.setTimestamp(new Date(System.currentTimeMillis()));
        message.setContent(dto.getContent());
        message.setStatus(EnumMessageStatus.DELIVERED.getValue());

        chatDAO.saveAndFlushMessage(message);

        return message;
    }

    public void seenUnreadMessages(String senderID, String recipientID) {
        chatRoomService.updateStatus(senderID, recipientID, EnumMessageStatus.RECEIVED.getValue());
    }

    public long countNewMessage(String senderId, String recipientId){
        return chatDAO.countNewMessage(senderId, recipientId);
    }
}