package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.live.LiveCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LiveCartRepository extends JpaRepository<LiveCart, String> {
    @Query("SELECT c FROM LiveCart c WHERE c.userId = ?1 and c.sessionId = ?2")
    Optional<LiveCart> findByUserId(String userId, String sessionId);

    @Query("SELECT c FROM LiveCart c WHERE " +
            "(?1 IS NULL AND c.userId = ?2) " +
            "OR c.liveCartId = ?1")
    Optional<LiveCart> findByLiveCartIdAndUserId(String liveCartId, String userId);

    @Query(value = "select c from LiveCart c where c.liveCartId = ?1")
    Optional<LiveCart> findByLiveCartId(String liveCartId);
}
