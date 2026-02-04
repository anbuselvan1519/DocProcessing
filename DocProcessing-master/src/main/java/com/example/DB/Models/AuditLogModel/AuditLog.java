package com.example.DB.Models.AuditLogModel;

import com.example.DB.Models.DocumentModel.Document;
import com.example.DB.Models.UserModel.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;

    @ManyToOne
    @JoinColumn(name = "performed_by")
    private User performedBy;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;

    private LocalDateTime timestamp;
    private String remarks;
}

