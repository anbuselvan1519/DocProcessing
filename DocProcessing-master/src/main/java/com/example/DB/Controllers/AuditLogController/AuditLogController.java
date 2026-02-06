package com.example.DB.Controllers.AuditLogController;

import com.example.DB.Models.AuditLogModel.AuditLog;
import com.example.DB.Models.UserModel.User;
import com.example.DB.Services.AuditLogService.AuditLogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping("/recent")
    public List<AuditLog> getRecentLogs(HttpSession session) {

        User user = (User) session.getAttribute("USER");
        if (user == null) {
            throw new IllegalStateException("User not logged in");
        }
        return auditLogService.getRecentActivity(user);
    }
}

