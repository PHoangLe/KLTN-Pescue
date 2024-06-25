package com.project.pescueshop.service.live;

import com.project.pescueshop.model.dto.LiveItemRequest;
import com.project.pescueshop.model.entity.Variety;
import com.project.pescueshop.model.entity.live.LiveItem;
import com.project.pescueshop.model.entity.live.LiveSession;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.LiveItemDAO;
import com.project.pescueshop.repository.dao.LiveSessionDAO;
import com.project.pescueshop.service.BaseService;
import com.project.pescueshop.service.VarietyService;
import com.project.pescueshop.util.constant.EnumLiveStatus;
import com.project.pescueshop.util.constant.EnumResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LiveItemService extends BaseService {
    private final LiveItemDAO liveItemDAO;
    private final VarietyService varietyService;
    private final LiveSessionDAO liveSessionDAO;

    public void addLiveItem(LiveSession liveSession, List<LiveItemRequest> liveItemList) throws FriendlyException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        try {
            List<CompletableFuture<Void>> futures = liveItemList.stream()
                    .map(liveItemRequest -> CompletableFuture.runAsync(() -> {
                        Variety variety = varietyService.findById(liveItemRequest.getVarietyId());

                        LiveItem liveItem = LiveItem.builder()
                                .liveSessionId(liveSession.getSessionId())
                                .varietyId(variety.getVarietyId())
                                .productId(variety.getProductId())
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
        } finally {
            executor.shutdown();
        }
    }

    public void addLiveItemAsync(LiveSession liveSession, List<LiveItemRequest> liveItemList) {
        CompletableFuture.runAsync(() -> {
            try {
                addLiveItem(liveSession, liveItemList);
            } catch (FriendlyException e) {
                log.error("Error creating connection: " + e.getMessage());
            }
        });
    }

    public void saveAndFlushLiveItem(LiveItem liveSession) {
        liveItemDAO.saveAndFlushLiveItem(liveSession);
    }

    public LiveItem findByLiveItemId(String liveItemId) {
        return liveItemDAO.findLiveItemByLiveItemId(liveItemId);
    }

    private List<LiveItem> getLiveItemBySessionId(String liveSessionId) {
        return liveItemDAO.findAllByLiveSessionId(liveSessionId);
    }

    public List<LiveItem> getLiveItemByLiveSessionId(String liveSessionId) throws FriendlyException {
        LiveSession liveSession = liveSessionDAO.findBySessionId(liveSessionId);

        if (liveSession == null || !Objects.equals(liveSession.getStatus(), EnumLiveStatus.ACTIVE.getValue())){
            throw new FriendlyException(EnumResponseCode.LIVE_SESSION_NOT_FOUND);
        }

        return getLiveItemBySessionId(liveSessionId);
    }
}
