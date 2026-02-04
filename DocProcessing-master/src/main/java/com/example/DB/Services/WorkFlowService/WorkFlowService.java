package com.example.DB.Services.WorkFlowService;

import com.example.DB.DTO.WorkflowResult;
import com.example.DB.Enums.DecisionResult;
import com.example.DB.Models.DecisionModel.Decision;
import com.example.DB.Models.DocumentModel.Document;
import com.example.DB.Models.ExtractionModel.Extraction;
import com.example.DB.Models.UserModel.User;
import com.example.DB.Repositories.DecisionRepository.DecisionRepository;
import com.example.DB.Repositories.DocumentRepository.DocumentRepository;
import com.example.DB.Repositories.ExtractionRepository.ExtractionRepository;
import com.example.DB.Services.AuditLogService.AuditLogService;
import com.example.DB.Services.DecisionService.DecisionService;
import com.example.DB.Services.DocumentProcessingService.DocumentNormalizerService;
import com.example.DB.Services.DocumentProcessingService.ExtractionService;
import com.example.DB.Services.DocumentProcessingService.ImageProcessingService;
import com.example.DB.Services.DocumentProcessingService.OcrService;
import jakarta.transaction.Transactional;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Service
@Transactional
public class WorkFlowService {

    private final DocumentNormalizerService documentNormalizerService;
    private final ImageProcessingService imageProcessingService;
    private final OcrService ocrService;
    private final ExtractionService extractionService;
    private final DecisionService decisionService;
    private final ExtractionRepository extractionRepository;
    private final DecisionRepository decisionRepository;
    private final AuditLogService auditLogService;
    private final DocumentRepository documentRepository;

    // Gson instance (safe, lightweight)
    private final Gson gson = new GsonBuilder().create();

    public WorkFlowService(
            DocumentNormalizerService documentNormalizerService,
            ImageProcessingService imageProcessingService,
            OcrService ocrService,
            ExtractionService extractionService,
            DecisionService decisionService,
            ExtractionRepository extractionRepository,
            DecisionRepository decisionRepository,
            AuditLogService auditLogService,
            DocumentRepository documentRepository
    ) {
        this.documentNormalizerService = documentNormalizerService;
        this.imageProcessingService = imageProcessingService;
        this.ocrService = ocrService;
        this.extractionService = extractionService;
        this.decisionService = decisionService;
        this.extractionRepository = extractionRepository;
        this.decisionRepository = decisionRepository;
        this.auditLogService = auditLogService;
        this.documentRepository = documentRepository;
    }

    public WorkflowResult processDocument(File file, User currentUser)
            throws IOException, TesseractException {

        if (file == null || !file.exists()) {
            throw new IOException("Invalid document provided");
        }

        List<File> images =
                documentNormalizerService.normalizeToImages(file);

        StringBuilder fullOcrText = new StringBuilder();

        for (File image : images) {

            File processedImage =
                    imageProcessingService.preprocessImage(image);

            String ocrText =
                    ocrService.extractTextFromImage(processedImage);

            fullOcrText.append(ocrText).append("\n");
        }

        Map<String, String> extractedFields =
                extractionService.extractFields(fullOcrText.toString());

        int confidenceScore =
                decisionService.calculateConfidenceScore(extractedFields);

        DecisionResult decision =
                decisionService.makeDecision(extractedFields);

        // -------------------------------
        // Persist Document
        // -------------------------------
        Document document = new Document();
        document.setFileName(file.getName());
        document.setFilePath(file.getAbsolutePath());
        document.setMimeType(Files.probeContentType(file.toPath()));
        document.setStatus("PROCESSED");
        document.setUploadedAt(LocalDateTime.now());
        document.setUploadedBy(currentUser);

        document = documentRepository.save(document);

        // -------------------------------
        // Persist Extraction (JSON via Gson)
        // -------------------------------
        Extraction extraction = new Extraction();
        extraction.setDocument(document);
        extraction.setExtractedJson(
                gson.toJson(extractedFields)   // ✅ REPLACEMENT
        );
        extraction.setConfidenceScore((double) confidenceScore);
        extraction.setExtractedAt(LocalDateTime.now());

        extractionRepository.save(extraction);

        // -------------------------------
        // Persist Decision
        // -------------------------------
        Decision decisionEntity = new Decision();
        decisionEntity.setDocument(document);
        decisionEntity.setThrustScore((double) confidenceScore);
        decisionEntity.setDecisionResult(decision.name());
        decisionEntity.setRuleSummary("Confidence-based decision");
        decisionEntity.setFinalizedAt(LocalDateTime.now());

        decisionRepository.save(decisionEntity);

        // -------------------------------
        // Audit log
        // -------------------------------
        auditLogService.logAction(
                "DOCUMENT_PROCESSED",
                currentUser,
                document,
                decision.name()
        );

        return new WorkflowResult(
                extractedFields,
                fullOcrText.toString(),
                confidenceScore,
                decision
        );
    }
}
