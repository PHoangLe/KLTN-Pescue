package com.project.pescueshop.repository;

import com.project.pescueshop.model.entity.ImportItem;
import com.project.pescueshop.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImportItemRepository extends JpaRepository<ImportItem, String> {
    @Query("SELECT ii FROM ImportItem ii WHERE ii.varietyId = ?1 AND ii.importInvoiceId = ?2")
    Optional<ImportItem> findImportItemByVarietyIdAndInvoiceId(String varietyId, String invoiceId);
}
