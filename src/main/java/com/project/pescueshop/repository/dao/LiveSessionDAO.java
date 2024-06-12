package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.entity.live.LiveSession;
import com.project.pescueshop.repository.jpa.LiveSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LiveSessionDAO {
    private final LiveSessionRepository liveSessionRepository;

    public void saveAndFlushLiveSession(LiveSession liveSession) {
        liveSessionRepository.save(liveSession);
    }

    public Page<LiveSession> getAllActiveLiveSession(Boolean isOnlyActive, Pageable pageable) {
        return liveSessionRepository.getAllActiveLiveSession(isOnlyActive, pageable);
    }

    public LiveSession findBySessionKey(String sessionKey) {
        return liveSessionRepository.findBySessionKey(sessionKey);
    }

    public LiveSession findBySessionId(String sessionId) {
        return liveSessionRepository.findById(sessionId).orElse(null);
    }
}
