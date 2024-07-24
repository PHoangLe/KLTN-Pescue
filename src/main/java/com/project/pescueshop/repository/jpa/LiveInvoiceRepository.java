package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.live.LiveInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LiveInvoiceRepository extends JpaRepository<LiveInvoice, String> {
    @Query("SELECT li FROM LiveInvoice li WHERE li.userId = ?1")
    List<LiveInvoice> findByUserId(String userId);

    @Query("SELECT li FROM LiveInvoice li " +
            "WHERE (li.status = ?4 OR ?4 is null) " +
            "AND (li.paymentType = ?5 OR ?5 IS NULL) " +
            "AND (li.merchantId = ?3 OR ?3 IS NULL) " +
            "AND li.createdDate >= ?1 and li.createdDate <= ?2")
    Page<LiveInvoice> findAllByStatusAndPaymentStatus(Date fromDate, Date toDate, String merchantId, String status, String paymentType, Pageable pageable);
}
