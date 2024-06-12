package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.live.LiveSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface LiveSessionRepository extends JpaRepository<LiveSession, String> {
    @Query("SELECT ls FROM LiveSession ls " +
            "WHERE ( ?1 = false OR ls.status = 'ACTIVE') " +
            "ORDER BY ls.startTime DESC")
    Page<LiveSession> getAllActiveLiveSession(Boolean isOnlyActive, Pageable pageable);

    @Query("SELECT ls FROM LiveSession ls WHERE ls.sessionKey = ?1")
    LiveSession findBySessionKey(String sessionKey);
}
