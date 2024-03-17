package com.project.pescueshop.repository.mapper;

import com.project.pescueshop.model.dto.ReportResultDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReportResultMapper implements RowMapper<ReportResultDTO> {
    @Override
    public ReportResultDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ReportResultDTO.builder()
                .reportTime(rs.getDate("report_date"))
                .totalSell(rs.getLong("total_final_price"))
                .totalImport(rs.getLong("total_import_price"))
                .build();
    }
}
