package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.live.LiveInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveInvoiceRepository extends JpaRepository<LiveInvoice, String> {
}
