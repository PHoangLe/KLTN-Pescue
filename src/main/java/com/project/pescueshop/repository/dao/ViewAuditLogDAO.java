package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.dto.InvoiceDataDTO;
import com.project.pescueshop.model.entity.ViewAuditLog;
import com.project.pescueshop.repository.jpa.ViewAuditLogRepository;
import com.project.pescueshop.repository.mapper.InvoiceDataMapper;
import com.project.pescueshop.util.Util;
import com.project.pescueshop.util.constant.EnumObjectType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ViewAuditLogDAO extends BaseDAO {
    private final ViewAuditLogRepository viewAuditLogRepository;
    private final InvoiceDataMapper invoiceDataMapper;

    public String saveAndFLushAudit(String objectId, String viewerId, EnumObjectType objectType){
        ViewAuditLog auditLog = ViewAuditLog.builder()
                .objectId(objectId)
                .objectType(objectType.toString())
                .viewerId(viewerId)
                .date(Util.getCurrentDate())
                .build();

        viewAuditLogRepository.saveAndFlush(auditLog);

        return auditLog.getViewAuditLogId();
    }

    public List<ViewAuditLog> getByObjectId(String objectId){
        return viewAuditLogRepository.findByObjectId(objectId);
    }

    public List<InvoiceDataDTO> getInvoiceData() {
        String sql = "select i.invoice_id, i.user_id, v.product_id " +
                "from invoice_item ii " +
                "join invoice i on i.invoice_id = ii.invoice_id " +
                "join variety v on v.variety_id = ii.variety_id; ";

        return jdbcTemplate.query(sql, invoiceDataMapper);
    }
}
