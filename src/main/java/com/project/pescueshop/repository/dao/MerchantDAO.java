package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.entity.Merchant;
import com.project.pescueshop.repository.inteface.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MerchantDAO extends BaseDAO{
    private final MerchantRepository merchantRepository;

    public void saveAndFlushMerchant(Merchant merchant){
        merchantRepository.save(merchant);
    }

    public Merchant getMerchantById(String merchantId) {
        return merchantRepository.findByMerchantId(merchantId);
    }

    public Merchant getMerchantByUserId(String userId) {
        return merchantRepository.findByUserId(userId);
    }

    public List<Merchant> getApprovedMerchant() {
        return merchantRepository.getApprovedMerchant();
    }

    public List<Merchant> getListMerchantForAdmin(boolean isApproved, boolean isSuspended, boolean isLiveable) {
        return merchantRepository.getListMerchantForAdmin(isApproved, isSuspended, isLiveable);
    }

    public void deleteMerchant(String merchantId) {
        merchantRepository.deleteByMerchantId(merchantId);
    }
}
