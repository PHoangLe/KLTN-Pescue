package com.project.pescueshop.service.live;

import com.project.pescueshop.model.entity.live.LiveInvoice;
import com.project.pescueshop.model.entity.live.LiveInvoiceItem;
import com.project.pescueshop.repository.dao.LiveInvoiceDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LiveInvoiceService {
    private final LiveInvoiceDAO liveInvoiceDAO;

    public void saveAndFlushLiveInvoice(LiveInvoice liveVoice) {
        liveInvoiceDAO.saveAndFlushLiveInvoice(liveVoice);
    }

    public void saveAndFlushLiveInvoiceItem(LiveInvoiceItem liveInvoiceItem) {
        liveInvoiceDAO.saveAndFlushLiveInvoiceItem(liveInvoiceItem);
    }

    public void addInvoiceItemsToInvoice(LiveInvoice liveInvoice, List<LiveInvoiceItem> liveInvoiceItems) {
        liveInvoiceItems.forEach(liveInvoiceItem -> {
            liveInvoiceItem.setLiveInvoiceId(liveInvoice.getLiveInvoiceId());
            saveAndFlushLiveInvoiceItem(liveInvoiceItem);
        });
    }
}
