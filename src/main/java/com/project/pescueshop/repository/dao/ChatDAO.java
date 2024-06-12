package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.entity.ChatMessage;
import com.project.pescueshop.model.entity.ChatRoom;
import com.project.pescueshop.repository.jpa.ChatMessageRepository;
import com.project.pescueshop.repository.jpa.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatDAO extends BaseDAO{
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public Optional<ChatRoom> findRoomByUser1AndUser2(String senderId, String recipientId) {
        return chatRoomRepository.findByMemberId(senderId, recipientId);
    }

    public void saveAndFlushRoom(ChatRoom newChatRoom) {
        chatRoomRepository.saveAndFlush(newChatRoom);
    }

    public List<ChatRoom> findAllRoomByUser(String userID) {
        return chatRoomRepository.findAllRoomByUser(userID);
    }

    public List<ChatMessage> findAllMessageByRoomID(String chatRoomID) {
        return chatMessageRepository.findAllMessageByRoomId(chatRoomID);
    }

    public void updateChatStatus(String senderId, String recipientId, String status) {
        String sql = "UPDATE chat_message " +
                "SET status = :p_status " +
                "WHERE sender_id = :p_sender_id " +
                "AND recipient_id = :p_recipient_id ";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_status", status)
                .addValue("p_sender_id", senderId)
                .addValue("p_recipient_id", recipientId);

        jdbcTemplate.update(sql, parameters);
    }

    public List<ChatMessage> findAllMessageBySenderIdAndRecipientId(String senderId, String recipientId) {
        return chatMessageRepository.findAllMessageBySenderIdAndRecipientId(senderId, recipientId);
    }

    public void saveAndFlushMessage(ChatMessage message) {
        chatMessageRepository.saveAndFlush(message);
    }

    public Long countNewMessage(String senderId, String recipientId) {
        String sql =
                " SELECT COALESCE(COUNT(cs.message_id), 0) " +
                        " FROM chat_message cs " +
                        " WHERE cs.sender_id = :p_sender_id " +
                        " AND cs.recipient_id = :p_recipient_id " +
                        " AND cs.status = 'DELIVERED' ";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_sender_id", senderId)
                .addValue("p_recipient_id", recipientId);

        return jdbcTemplate.queryForObject(sql, parameters, Long.class);
    }
}
