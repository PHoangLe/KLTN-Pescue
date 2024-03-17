package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.ReportResultDTO;
import com.project.pescueshop.repository.dao.StatisticDAO;
import com.project.pescueshop.util.constant.EnumGroupType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticService {
    private final StatisticDAO statisticDAO;

    public List<ReportResultDTO> getReportByDateRange(Date fromDate, Date toDate, String groupType){
        EnumGroupType enumGroupType = EnumGroupType.getByValue(groupType);
        return statisticDAO.getReportByDateRange(fromDate, toDate, enumGroupType.getGranularity());
    }
}
