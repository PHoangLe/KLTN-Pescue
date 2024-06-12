package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.live.LiveInvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiveInvoiceItemRepository extends JpaRepository<LiveInvoiceItem, String>{
    @Query("SELECT lii FROM LiveInvoiceItem lii WHERE lii.liveInvoiceId = ?1")
    List<LiveInvoiceItem> findByInvoiceId(String liveInvoiceId);
}
