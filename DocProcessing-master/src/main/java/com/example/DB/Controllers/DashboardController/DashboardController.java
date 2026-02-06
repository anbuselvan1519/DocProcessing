package com.example.DB.Controllers.DashboardController;

import com.example.DB.Models.AuditLogModel.AuditLog;
import com.example.DB.Models.DecisionModel.Decision;
import com.example.DB.Models.DocumentModel.Document;
import com.example.DB.Models.UserModel.User;
import com.example.DB.Services.AuditLogService.AuditLogService;
import com.example.DB.Services.DasboardService.DashboardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService,
            AuditLogService auditLogService
    ) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/recent")
    public List<AuditLog> getRecentActivity(HttpSession session) {

        User user = (User) session.getAttribute("USER");
        if (user == null) {
            throw new RuntimeException("User not logged in");
        }

        return dashboardService.getRecentAuditLogs(user);
    }

    @GetMapping("/documents/recent")
    public List<Document> getRecentDocuments(HttpSession session) {

        ensureLoggedIn(session);
        return dashboardService.getRecentDocuments();
    }

    @GetMapping("/decisions/recent")
    public List<Decision> getRecentDecisions(HttpSession session) {

        ensureLoggedIn(session);
        return dashboardService.getRecentDecisions();
    }

    @GetMapping("/analytics")
    public Map<String, Long> getAnalytics(HttpSession session) {

        ensureLoggedIn(session);
        return dashboardService.getAnalytics();
    }

    private void ensureLoggedIn(HttpSession session) {

        User user = (User) session.getAttribute("USER");
        if (user == null) {
            throw new IllegalStateException("User not logged in");
        }
    }
}
