package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.ViewAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewAuditLogRepository extends JpaRepository<ViewAuditLog, String> {
    @Query("SELECT v FROM ViewAuditLog v WHERE v.objectId = ?1")
    List<ViewAuditLog> findByObjectId(String objectId);
}
