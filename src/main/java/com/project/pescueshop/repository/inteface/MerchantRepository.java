package com.project.pescueshop.repository.inteface;

import com.project.pescueshop.model.entity.Merchant;
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
            "(?1 = false OR m.isApproved = true) AND " +
            "(?2 = false OR m.isSuspended = true) AND " +
            "(?3 = false OR m.isLiveable = true)")
    List<Merchant> getListMerchantForAdmin(boolean isApproved, boolean isSuspended, boolean isLiveable);
}
