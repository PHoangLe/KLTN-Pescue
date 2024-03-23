package com.project.pescueshop.repository.inteface;

import com.project.pescueshop.model.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, String> {
    @Query(value = "SELECT v.* " +
            "FROM voucher v " +
            "JOIN users u ON u.user_id = ?1 " +
            "AND u.member_point > v.price " +
            "WHERE min_invoice_value > 0  " +
            "AND v.status = 'ACTIVE' ", nativeQuery = true)
    List<Voucher> findAllAvailabeVoucher(String userId);
}
