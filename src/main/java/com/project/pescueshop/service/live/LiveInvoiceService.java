package com.project.pescueshop.service.live;

import com.project.pescueshop.model.entity.Invoice;
import com.project.pescueshop.model.entity.Merchant;
import com.project.pescueshop.model.entity.live.LiveInvoice;
import com.project.pescueshop.model.entity.live.LiveInvoiceItem;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.LiveInvoiceDAO;
import com.project.pescueshop.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LiveInvoiceService {
    private final LiveInvoiceDAO liveInvoiceDAO;
    private final AuthenticationService authenticationService;

    public void saveAndFlushLiveInvoice(LiveInvoice liveVoice) {
        liveInvoiceDAO.saveAndFlushLiveInvoice(liveVoice);
    }

    public Page<LiveInvoice> getAllLiveInvoice(Date fromDate, Date toDate, Integer page, Integer size, String status, String paymentMethod) throws FriendlyException {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<LiveInvoice> resp;

        if (!AuthenticationService.isCurrentAdmin()){
            Merchant merchant = authenticationService.getCurrentMerchant();
            resp = liveInvoiceDAO.getAllLiveInvoice(fromDate, toDate, pageable, status, paymentMethod, merchant.getMerchantId());
        } else {
            resp = liveInvoiceDAO.getAllLiveInvoice(fromDate, toDate, pageable, status, paymentMethod, null);
        }

        return resp;
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
