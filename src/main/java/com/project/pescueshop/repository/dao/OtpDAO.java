package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.entity.Otp;
import com.project.pescueshop.repository.jpa.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OtpDAO extends BaseDAO{
    private final OtpRepository otpRepository;

    public void saveAndFlushOtp(Otp otp){
        otpRepository.saveAndFlush(otp);
    }

    public Otp findOTPConfirmEmailByUserId(String userId, String type) {
        return otpRepository.findOTPConfirmEmailByUserId(userId, type);
    }
}
