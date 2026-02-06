package com.example.DB.Models.DecisionModel;

import com.example.DB.Models.DocumentModel.Document;
import com.example.DB.Models.UserModel.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "decisions")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Decision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "document_id")
    private Document document;

    private Double thrustScore;
    private String decisionResult;
    private String ruleSummary;

    @Column(columnDefinition = "TEXT")
    private String manualComment;

    @Column(name = "reviewed_by_user_id", nullable = false)
    private Long reviewedByUserId;

    @Column(name = "reviewed_by_role", nullable = false)
    private String reviewedByRole;

    private LocalDateTime finalizedAt;
}

