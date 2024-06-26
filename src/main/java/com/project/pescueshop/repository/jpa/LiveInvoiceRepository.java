package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.live.LiveInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiveInvoiceRepository extends JpaRepository<LiveInvoice, String> {
    @Query("SELECT li FROM LiveInvoice li WHERE li.userId = ?1")
    List<LiveInvoice> findByUserId(String userId);
}
