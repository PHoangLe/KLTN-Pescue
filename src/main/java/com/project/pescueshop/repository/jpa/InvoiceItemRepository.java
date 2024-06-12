package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, String> {
    @Query(
            "select ii " +
            "from InvoiceItem ii " +
            "where ii.invoiceId = ?1 "
    )
    List<InvoiceItem> findInvoiceItemByInvoiceId(String invoiceId);
}
