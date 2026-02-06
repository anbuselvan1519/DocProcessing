package com.example.DB.Services.AuditLogService;

import org.springframework.stereotype.Service;

import com.example.DB.Models.AuditLogModel.AuditLog;
import com.example.DB.Models.DocumentModel.Document;
import com.example.DB.Models.UserModel.User;
import com.example.DB.Repositories.AuditLogRepository.AuditLogRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(
            String action,
            User user,
            Document document,
            String remarks
    ) {

        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setPerformedByUserRole(user.getRole());
        log.setPerformedByUserId(user.getId());
        log.setDocument(document);
        log.setRemarks(remarks);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }

    public List<AuditLog> getRecentActivity(User user) {
        return auditLogRepository
                .findTop10ByPerformedByUserIdOrderByTimestampDesc(user.getId());
    }


    public long countByAction(String action) {
        return auditLogRepository.countByAction(action);
    }
}
