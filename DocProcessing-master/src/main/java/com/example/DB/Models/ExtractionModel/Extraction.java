package com.example.DB.Models.ExtractionModel;

import com.example.DB.Models.DocumentModel.Document;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "extractions")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Extraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "document_id")
    private Document document;

    @Column(columnDefinition = "TEXT")
    private String extractedJson;

    private Double confidenceScore;
    private LocalDateTime extractedAt;
}

