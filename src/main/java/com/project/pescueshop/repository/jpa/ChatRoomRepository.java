package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    @Query(value = "SELECT * " +
            "FROM chat_room cr " +
            "WHERE (cr.first_user_id = ?1 AND cr.second_user_id = ?2) " +
            "OR (cr.first_user_id = ?2 AND cr.second_user_id = ?1)", nativeQuery = true)
    Optional<ChatRoom> findByMemberId(String senderId, String recipientId);

    @Query(value = "SELECT * " +
            "FROM chat_room cr " +
            "WHERE (cr.first_user_id = ?1 OR cr.second_user_id = ?1) ", nativeQuery = true)
    List<ChatRoom> findAllRoomByUser(String userId);
}
