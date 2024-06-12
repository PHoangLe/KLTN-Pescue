package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.ErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLog, String> {
}
