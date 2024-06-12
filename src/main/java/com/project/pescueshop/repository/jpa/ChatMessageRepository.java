package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
    @Query(value = "SELECT * " +
            "FROM chat_message cm " +
            "WHERE cm.chat_room_id = ?1 " +
            "ORDER BY cm.timestamp ASC ", nativeQuery = true)
    List<ChatMessage> findAllMessageByRoomId(String chatRoomId);

    @Modifying
    @Query(value = "UPDATE " +
            "chat_message cm " +
            "SET cm.status = ?3 " +
            "WHERE cm.sender_id = ?1 " +
            "AND recipient_id = ?2 ", nativeQuery = true)
    void updateChatStatus(String senderId, String recipientId, String status);

    @Query(value = "SELECT * " +
            "FROM chat_message cm " +
            "WHERE cm.sender_id = ?1 " +
            "AND cm.recipient_id = ?2 ", nativeQuery = true)
    List<ChatMessage> findAllMessageBySenderIdAndRecipientId(String senderId, String recipientId);
}
