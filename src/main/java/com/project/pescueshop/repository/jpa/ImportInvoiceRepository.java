package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.ImportInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ImportInvoiceRepository extends JpaRepository<ImportInvoice, String> {
    @Query("select ii from ImportInvoice ii where ii.createdDate >= ?1 and ii.createdDate <= ?2")
    List<ImportInvoice> findImportInvoiceByDateRange(Date fromDate, Date toDate);
}
