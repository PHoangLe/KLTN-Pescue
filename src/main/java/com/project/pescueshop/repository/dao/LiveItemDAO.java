package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.entity.live.LiveItem;
import com.project.pescueshop.repository.inteface.LiveItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LiveItemDAO {
    private final LiveItemRepository liveItemRepository;

    public void saveLiveItem(LiveItem liveItem) {
        liveItemRepository.save(liveItem);
    }
}
