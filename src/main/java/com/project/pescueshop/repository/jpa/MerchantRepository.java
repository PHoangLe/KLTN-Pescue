package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String>{
    @Query("select m from Merchant m where m.merchantId = ?1")
    Merchant findByMerchantId(String merchantId);

    @Query("select m from Merchant m where m.userId = ?1")
    Merchant findByUserId(String userId);

    @Query("delete from Merchant m where m.merchantId = ?1")
    void deleteByMerchantId(String merchantId);

    @Query("select m from Merchant m where m.isApproved = true")
    List<Merchant> getApprovedMerchant();

    @Query("SELECT m FROM Merchant m WHERE " +
            "(?1 IS NULL OR m.isApproved = ?1) AND " +
            "(?2 IS NULL OR  m.isSuspended = ?2) AND " +
            "(?3 IS NULL OR  m.isLiveable = ?3)")
    Page<Merchant> getListMerchantForAdmin(Boolean isApproved, Boolean isSuspended, Boolean isLiveable, Pageable pageable);
}
