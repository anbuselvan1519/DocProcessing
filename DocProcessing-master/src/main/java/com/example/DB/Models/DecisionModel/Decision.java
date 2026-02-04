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

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    private LocalDateTime finalizedAt;
}

