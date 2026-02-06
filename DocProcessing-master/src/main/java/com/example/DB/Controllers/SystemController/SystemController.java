package com.example.DB.Controllers.SystemController;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
public class SystemController {

    @GetMapping("/health")
    public Map<String, Object> health() {

        Map<String, Object> status = new HashMap<>();
        status.put("status", "Application running successfully");
        status.put("timestamp", LocalDateTime.now());
        return status;
    }

    @GetMapping("/version")
    public Map<String, String> version() {

        Map<String, String> versionInfo = new HashMap<>();
        versionInfo.put("application", "AI Document Processing System");
        versionInfo.put("version", "1.0.0");

        return versionInfo;
    }

    @GetMapping("/info")
    public Map<String, String> systemInfo() {

        Map<String, String> info = new HashMap<>();
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("os", System.getProperty("os.name"));
        info.put("environment", "Local");

        return info;
    }
}
