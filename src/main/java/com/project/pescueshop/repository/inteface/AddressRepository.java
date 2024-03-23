package com.project.pescueshop.repository.inteface;

import com.project.pescueshop.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    @Query("SELECT a FROM Address a WHERE a.userId = ?1 AND a.status = 'ACTIVE'")
    List<Address> findAddressByUserId(String userId);
}
