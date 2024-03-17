package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.CreateRatingDTO;
import com.project.pescueshop.model.dto.ReportResultDTO;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.entity.Rating;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.AuthenticationService;
import com.project.pescueshop.service.StatisticService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    @PostMapping("")
    public ResponseEntity<ResponseDTO<List<ReportResultDTO>>> report(
            @RequestParam Date fromDate,
            @RequestParam Date toDate,
            @RequestParam String groupType
            ) throws FriendlyException {
        List<ReportResultDTO> reportByDateRange = statisticService.getReportByDateRange(fromDate, toDate, groupType);

        ResponseDTO<List<ReportResultDTO>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, reportByDateRange);
        return ResponseEntity.ok(result);
    }
}
