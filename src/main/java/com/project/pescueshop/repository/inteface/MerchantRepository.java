package com.project.pescueshop.repository.inteface;

import com.project.pescueshop.model.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String>{
}
