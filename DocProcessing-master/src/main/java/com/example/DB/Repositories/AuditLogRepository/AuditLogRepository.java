package com.example.DB.Repositories.AuditLogRepository;

import com.example.DB.Models.AuditLogModel.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.example.DB.Models.UserModel.User;
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Recent activity for a specific user
    List<AuditLog> findTop10ByPerformedByOrderByTimestampDesc(User performedBy);

    // Optional: global recent activity
    List<AuditLog> findTop10ByOrderByTimestampDesc();

    long count();

    long countByAction(String action);
}


