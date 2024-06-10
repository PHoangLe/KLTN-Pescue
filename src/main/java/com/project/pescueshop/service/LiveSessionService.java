package com.project.pescueshop.service;

import com.project.pescueshop.model.entity.live.LiveSession;
import com.project.pescueshop.repository.dao.LiveSessionDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LiveSessionService extends BaseService{
    private final LiveSessionDAO liveSessionDAO;

    public void saveAndFlushLiveSession(LiveSession liveSession) {
        liveSessionDAO.saveAndFlushLiveSession(liveSession);
    }

    public Page<LiveSession> getAllActiveLiveSession(Boolean isOnlyActive, Pageable pageable) {
        return liveSessionDAO.getAllActiveLiveSession(isOnlyActive, pageable);
    }
}
