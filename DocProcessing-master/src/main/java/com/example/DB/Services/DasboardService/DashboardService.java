package com.example.DB.Services.DasboardService;

import com.example.DB.Models.AuditLogModel.AuditLog;
import com.example.DB.Models.DecisionModel.Decision;
import com.example.DB.Models.DocumentModel.Document;
import com.example.DB.Models.UserModel.User;
import com.example.DB.Repositories.AuditLogRepository.AuditLogRepository;
import com.example.DB.Repositories.DecisionRepository.DecisionRepository;
import com.example.DB.Repositories.DocumentRepository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final DocumentRepository documentRepository;
    private final DecisionRepository decisionRepository;
    private final AuditLogRepository auditLogRepository;

    public DashboardService(
            DocumentRepository documentRepository,
            DecisionRepository decisionRepository,
            AuditLogRepository auditLogRepository
    ) {
        this.documentRepository = documentRepository;
        this.decisionRepository = decisionRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public List<Document> getRecentDocuments() {
        return documentRepository.findTop10ByOrderByUploadedAtDesc();
    }

    public List<Decision> getRecentDecisions() {
        return decisionRepository.findTop10ByOrderByFinalizedAtDesc();
    }

    public List<AuditLog> getRecentAuditLogs(User user) {
        return auditLogRepository
                .findTop10ByPerformedByUserIdOrderByTimestampDesc(user.getId());
    }

    public Map<String, Long> getAnalytics() {

        Map<String, Long> analytics = new HashMap<>();

        long totalDocuments = documentRepository.count();
        long autoApproved = decisionRepository.countByDecisionResult("AUTO_APPROVED");
        long manualReview = decisionRepository.countByDecisionResult("MANUAL_REVIEW");

        analytics.put("totalDocuments", totalDocuments);
        analytics.put("autoApproved", autoApproved);
        analytics.put("manualReview", manualReview);

        return analytics;
    }
}
