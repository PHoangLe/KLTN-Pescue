package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.entity.Merchant;
import com.project.pescueshop.repository.inteface.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MerchantDAO extends BaseDAO{
    private final MerchantRepository merchantRepository;

    public void saveAndFlushMerchant(Merchant merchant){
        merchantRepository.save(merchant);
    }
}
