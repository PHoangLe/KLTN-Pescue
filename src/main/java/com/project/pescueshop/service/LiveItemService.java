package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.LiveItemRequest;
import com.project.pescueshop.model.entity.Variety;
import com.project.pescueshop.model.entity.live.LiveItem;
import com.project.pescueshop.model.entity.live.LiveSession;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.LiveItemDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LiveItemService extends BaseService{
    private final LiveItemDAO liveItemDAO;
    private final VarietyService varietyService;

    public void addLiveItem(LiveSession liveSession, List<LiveItemRequest> liveItemList) throws FriendlyException {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<CompletableFuture<Void>> futures = liveItemList.stream()
                .map(liveItemRequest -> CompletableFuture.runAsync(() -> {
                    Variety variety = varietyService.findById(liveItemRequest.getVarietyId());

                    LiveItem liveItem = LiveItem.builder()
                            .liveSessionId(liveSession.getSessionId())
                            .varietyId(variety.getVarietyId())
                            .name(variety.getName())
                            .initialPrice(variety.getPrice())
                            .livePrice(liveItemRequest.getLivePrice())
                            .initialStock(liveItemRequest.getLiveStock())
                            .currentStock(liveItemRequest.getLiveStock())
                            .coverImage(varietyService.getCoverImageById(variety.getVarietyId()))
                            .build();

                    saveAndFlushLiveItem(liveItem);
                }, executor))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        executor.shutdown();
    }

    public void saveAndFlushLiveItem(LiveItem liveSession) {
        liveItemDAO.saveLiveItem(liveSession);
    }
}
