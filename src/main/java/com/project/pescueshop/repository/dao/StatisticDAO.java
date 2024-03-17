package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.dto.ReportResultDTO;
import com.project.pescueshop.repository.mapper.ReportResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatisticDAO extends BaseDAO{
    private final ReportResultMapper reportResultMapper;

    public List<ReportResultDTO> getReportByDateRange(Date fromDate, Date toDate, String groupType){
        String sql = "SELECT * FROM generate_report(:fromDate, :toDate, :granularity);";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("fromDate", fromDate)
                .addValue("toDate", toDate)
                .addValue("granularity", groupType);

        return jdbcTemplate.query(sql, parameters, reportResultMapper);
    }

}
