package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.OtpService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/otp")
@CrossOrigin
@RequiredArgsConstructor
@Api
public class OtpController {
    private final OtpService otpService;

    @PostMapping("/sendOTPConfirmEmail")
    public ResponseEntity<Object> sendOTPConfirmEmail(@RequestParam String emailAdrress) throws FriendlyException {
        otpService.sendOtpConfirmEmail(emailAdrress);

        ResponseDTO<String> result = new ResponseDTO<>(EnumResponseCode.SUCCESS);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/validateOTPConfirmEmail")
    public ResponseEntity<Object> validateOTPConfirmEmail(@RequestParam String emailAddress, @RequestParam String otp) throws FriendlyException {
        otpService.validateOtpConfirmEmail(emailAddress, otp);
        ResponseDTO<String> result = new ResponseDTO<>(EnumResponseCode.SUCCESS);
        return ResponseEntity.ok(result);
    }

    //Forgot Password
    @PostMapping("/sendOTPForgotPassword")
    public ResponseEntity<Object> sendOTPForgotPassword(@RequestParam String emailAddress) throws FriendlyException {
        otpService.sendOtpForgotPassword(emailAddress);
        ResponseDTO<String> result = new ResponseDTO<>(EnumResponseCode.SUCCESS);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/validateOTPForgotPassword")
    public ResponseEntity<Object> validateOTPForgotPassword(@RequestParam String emailAddress, @RequestParam String otp, @RequestParam String newPassword) throws FriendlyException {
        otpService.validateOtpForgotPassword(emailAddress, otp, newPassword);
        ResponseDTO<String> result = new ResponseDTO<>(EnumResponseCode.SUCCESS);
        return ResponseEntity.ok(result);
    }
}
