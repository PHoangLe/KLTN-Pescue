package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.entity.live.LiveInvoice;
import com.project.pescueshop.model.entity.live.LiveInvoiceItem;
import com.project.pescueshop.repository.jpa.LiveInvoiceItemRepository;
import com.project.pescueshop.repository.jpa.LiveInvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class LiveInvoiceDAO {
    private final LiveInvoiceRepository liveInvoiceRepository;
    private final LiveInvoiceItemRepository liveInvoiceItemRepository;

    public void saveAndFlushLiveInvoice(LiveInvoice liveInvoice) {
        liveInvoiceRepository.saveAndFlush(liveInvoice);
    }

    public void saveAndFlushLiveInvoiceItem(LiveInvoiceItem liveInvoiceItem) {
        liveInvoiceItemRepository.saveAndFlush(liveInvoiceItem);
    }

    public LiveInvoice findById(String invoiceId) {
        return liveInvoiceRepository.findById(invoiceId).orElse(null);
    }

    public List<LiveInvoiceItem> findItemsByLiveInvoiceId(String liveInvoiceId) {
        return liveInvoiceItemRepository.findByInvoiceId(liveInvoiceId);
    }
}
