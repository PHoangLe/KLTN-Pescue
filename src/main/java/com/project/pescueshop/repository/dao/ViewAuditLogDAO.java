package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.entity.ViewAuditLog;
import com.project.pescueshop.repository.jpa.ViewAuditLogRepository;
import com.project.pescueshop.util.Util;
import com.project.pescueshop.util.constant.EnumObjectType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ViewAuditLogDAO {
    private final ViewAuditLogRepository viewAuditLogRepository;

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
}
