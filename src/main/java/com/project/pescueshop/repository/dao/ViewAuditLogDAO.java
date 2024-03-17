package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.entity.ViewAuditLog;
import com.project.pescueshop.repository.ViewAuditLogRepository;
import com.project.pescueshop.util.Util;
import com.project.pescueshop.util.constant.EnumObjectType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ViewAuditLogDAO {
    private final ViewAuditLogRepository viewAuditLogRepository;

    public String saveAndFLushAudit(String objectId, EnumObjectType objectType){
        ViewAuditLog auditLog = ViewAuditLog.builder()
                .objectId(objectId)
                .objectType(objectType.toString())
                .date(Util.getCurrentDate())
                .build();

        viewAuditLogRepository.saveAndFlush(auditLog);

        return auditLog.getViewAuditLogId();
    }
}
