package com.project.pescueshop.repository.inteface;

import com.project.pescueshop.model.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String>{
    @Query("select m from Merchant m where m.merchantId = ?1")
    Merchant findByMerchantId(String merchantId);

    @Query("select m from Merchant m where m.userId = ?1")
    Merchant findByUserId(String userId);

    @Query("delete from Merchant m where m.merchantId = ?1")
    void deleteByMerchantId(String merchantId);
}
