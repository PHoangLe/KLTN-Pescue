package com.project.pescueshop.repository;

import com.project.pescueshop.model.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<Otp, String> {
    @Query(value = "SELECT * " +
            "FROM otp o " +
            "WHERE o.user_id = ?1 " +
            "AND o.type = ?2 " +
            "ORDER BY o.created_date DESC " +
            "LIMIT 1 ", nativeQuery = true)
    Otp findOTPConfirmEmailByUserId(String userId, String type);
}
