package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.ReportResultDTO;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.StatisticService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statistic")
@RequiredArgsConstructor
@CrossOrigin
@Api
public class StatisticController {
    private final StatisticService statisticService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/admin")
    public ResponseEntity<ResponseDTO<List<ReportResultDTO>>> reportForAdmin(
            @RequestParam("start_time_millis") long startTime,
            @RequestParam("end_time_millis") long endTime,
            @RequestParam(defaultValue = "month") String interval
            ) throws IOException {
        List<ReportResultDTO> reportByDateRange = statisticService.revenueStatisticForAdmin(startTime, endTime, interval);

        ResponseDTO<List<ReportResultDTO>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, reportByDateRange);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAuthority('ROLE_MERCHANT')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/merchant")
    public ResponseEntity<ResponseDTO<List<ReportResultDTO>>> reportForMerchant(
            @RequestParam("start_time_millis") long startTime,
            @RequestParam("end_time_millis") long endTime,
            @RequestParam(defaultValue = "month") String interval
    ) throws IOException, FriendlyException {
        List<ReportResultDTO> reportByDateRange = statisticService.revenueStatusForMerchant(startTime, endTime, interval);

        ResponseDTO<List<ReportResultDTO>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, reportByDateRange);
        return ResponseEntity.ok(result);
    }
}
