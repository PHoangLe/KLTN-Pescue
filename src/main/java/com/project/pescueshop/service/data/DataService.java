package com.project.pescueshop.service.data;

import com.project.pescueshop.model.dto.InvoiceDataDTO;
import com.project.pescueshop.model.entity.ViewAuditLog;
import com.project.pescueshop.repository.dao.ViewAuditLogDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataService {
    private final ViewAuditLogDAO viewAuditLogDAO;

    public List<ViewAuditLog> getViewsAudiLogData(String objectId){
        return viewAuditLogDAO.getByObjectId(objectId);
    }

    public List<InvoiceDataDTO> getInvoiceData(){
        return viewAuditLogDAO.getInvoiceData();
    }
}
