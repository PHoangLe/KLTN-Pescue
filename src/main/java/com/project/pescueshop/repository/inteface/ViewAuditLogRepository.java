package com.project.pescueshop.repository.inteface;

import com.project.pescueshop.model.entity.ViewAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewAuditLogRepository extends JpaRepository<ViewAuditLog, String> {
}
