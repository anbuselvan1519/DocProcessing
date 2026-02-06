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

    @Column(name = "performed_by_user_id", nullable = false)
    private Long performedByUserId;

    @Column(name = "performed_by_role", nullable = false)
    private String performedByUserRole;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;

    private LocalDateTime timestamp;
    private String remarks;
}

