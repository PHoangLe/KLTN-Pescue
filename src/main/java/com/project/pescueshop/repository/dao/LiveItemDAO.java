package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.entity.live.LiveItem;
import com.project.pescueshop.repository.jpa.LiveItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LiveItemDAO {
    private final LiveItemRepository liveItemRepository;

    public void saveAndFlushLiveItem(LiveItem liveItem) {
        liveItemRepository.save(liveItem);
    }

    public LiveItem findLiveItemByLiveItemId(String liveItemId) {
        return liveItemRepository.findById(liveItemId).orElse(null);
    }
}
