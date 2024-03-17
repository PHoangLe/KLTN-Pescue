package com.project.pescueshop.service;

import com.project.pescueshop.model.entity.Otp;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.OtpDAO;
import com.project.pescueshop.util.Util;
import com.project.pescueshop.util.constant.EnumOtpType;
import com.project.pescueshop.util.constant.EnumResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OtpService {
    private static final long OTP_EXPIRATION_TIME = 60 * 5; //seconds
    private static final int OTP_LENGTH = 6;
    private final UserService userService;
    private final OtpDAO otpDAO;
    private final EmailService emailService;
    public boolean isNumeric(String otp) {
        try {
            Double.parseDouble(otp);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public boolean isOtpValid(String otp){
        return otp.length() == 6 && isNumeric(otp);
    }

    private String generateOTP() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < OTP_LENGTH; i++){
            builder.append(random.nextInt(0, 9));
        }

        return builder.toString();
    }

    public void sendOtpConfirmEmail(String emailAddress) throws FriendlyException {
        User user = userService.findByEmail(emailAddress);

        if (user == null) {
            throw new FriendlyException(EnumResponseCode.USER_NOT_FOUND);
        }

        String otp = generateOTP();
        String emailBody = "Your OTP Code is: " + otp;

        emailService.sendMail(emailAddress, emailBody, "Email Confirmation");

        Otp otpConfirmEmail = Otp.builder()
                .code(otp)
                .createdDate(Util.getCurrentDate())
                .expiry(Util.getCurrentDatePlusSeconds(OTP_EXPIRATION_TIME))
                .userId(user.getUserId())
                .type(EnumOtpType.CONFIRM_EMAIL.getValue())
                .build();

        otpDAO.saveAndFlushOtp(otpConfirmEmail);
    }

    public void sendOtpForgotPassword(String emailAddress) throws FriendlyException {
        User user = userService.findByEmail(emailAddress);

        if (user == null) {
            throw new FriendlyException(EnumResponseCode.USER_NOT_FOUND);
        }

        String otp = generateOTP();
        String emailBody = "Your OTP Code is: " + otp;

        emailService.sendMail(emailAddress, emailBody, "Password Reset");

        Otp otpConfirmEmail = Otp.builder()
                .code(otp)
                .createdDate(Util.getCurrentDate())
                .expiry(Util.getCurrentDatePlusSeconds(OTP_EXPIRATION_TIME))
                .userId(user.getUserId())
                .type(EnumOtpType.FORGOT_PASSWORD.getValue())
                .build();

        otpDAO.saveAndFlushOtp(otpConfirmEmail);
    }

    public void validateOtp(Otp correctOtp, String validateOtp) throws FriendlyException {
        if(!isOtpValid(validateOtp) || validateOtp.isEmpty()) {
            throw new FriendlyException(EnumResponseCode.OTP_INVALID);
        }

        if (!validateOtp.equals(correctOtp.getCode())) {
            throw new FriendlyException(EnumResponseCode.OTP_INCORRECT);
        }

        if (correctOtp.getExpiry().before(Util.getCurrentDate())) {
            throw new FriendlyException(EnumResponseCode.OTP_EXPIRED);
        }
    }

    public void validateOtpConfirmEmail(String emailAddress, String otpCode) throws FriendlyException {
        User user = userService.findByEmail(emailAddress);
        Otp correctDto = otpDAO.findOTPConfirmEmailByUserId(user.getUserId(), EnumOtpType.CONFIRM_EMAIL.getValue());
        validateOtp(correctDto, otpCode);
        userService.unlockUser(user);
    }

    public void validateOtpForgotPassword(String emailAddress, String otpCode, String newPassword) throws FriendlyException {
        User user = userService.findByEmail(emailAddress);
        Otp correctDto = otpDAO.findOTPConfirmEmailByUserId(user.getUserId(), EnumOtpType.FORGOT_PASSWORD.getValue());
        validateOtp(correctDto, otpCode);
        userService.resetPassword(user, newPassword);
    }
}
