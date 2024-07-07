package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.dto.InvoiceItemDTO;
import com.project.pescueshop.model.entity.live.LiveInvoice;
import com.project.pescueshop.model.entity.live.LiveInvoiceItem;
import com.project.pescueshop.repository.jpa.LiveInvoiceItemRepository;
import com.project.pescueshop.repository.jpa.LiveInvoiceRepository;
import com.project.pescueshop.repository.mapper.InvoiceItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class LiveInvoiceDAO extends BaseDAO {
    private final LiveInvoiceRepository liveInvoiceRepository;
    private final LiveInvoiceItemRepository liveInvoiceItemRepository;

    private final InvoiceItemMapper invoiceItemMapper;

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

    public List<LiveInvoice> findAllLiveInvoiceByUserId(String userId) {
        return liveInvoiceRepository.findByUserId(userId);
    }

    public List<InvoiceItemDTO> getLiveInvoiceDetail(String liveInvoiceId){
        String sql = "SELECT i.user_id, m.user_id as merchant_user_id, i.live_invoice_id as invoice_id, ii.quantity, " +
                "           ii.total_price as total_price, v.variety_id, v.name, v.merchant_id, v.product_id, v.price as unit_price, " +
                "           (SELECT pi.images FROM product_images pi WHERE v.product_id = pi.product_product_id LIMIT 1) as image, v.stock_amount " +
                "            , v.weight, v.width, v.length, v.height " +
                "    FROM live_invoice i " +
                "    JOIN merchant m ON m.merchant_id = i.merchant_id " +
                "    JOIN live_invoice_item ii ON i.live_invoice_id = ii.live_invoice_id " +
                "    JOIN live_item li ON ii.live_item_id = li.live_item_id " +
                "    JOIN variety v ON li.variety_id = v.variety_id " +
                "    WHERE i.live_invoice_id = :p_live_invoice_id; ";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_live_invoice_id", liveInvoiceId);

        return jdbcTemplate.query(sql, parameters, invoiceItemMapper);
    }

    public List<LiveInvoiceItem> findAllLiveInvoiceItem() {
        return liveInvoiceItemRepository.findAll();
    }
}
