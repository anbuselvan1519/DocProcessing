package com.example.DB.Controllers.DecisionController;

import com.example.DB.Enums.DecisionResult;
import com.example.DB.Models.DecisionModel.Decision;
import com.example.DB.Models.DocumentModel.Document;
import com.example.DB.Models.UserModel.User;
import com.example.DB.Repositories.DecisionRepository.DecisionRepository;
import com.example.DB.Repositories.DocumentRepository.DocumentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/decision")
public class DecisionController {

    private final DecisionRepository decisionRepository;
    private final DocumentRepository documentRepository;

    public DecisionController(
            DecisionRepository decisionRepository,
            DocumentRepository documentRepository) {

        this.decisionRepository = decisionRepository;
        this.documentRepository = documentRepository;
    }

    @GetMapping("/{documentId}")
    public Decision getDecision(@PathVariable Long documentId) {

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Document not found")
                );

        return decisionRepository.findByDocument(document)
                .orElseThrow(() ->
                        new EntityNotFoundException("Decision not found for document")
                );
    }


    @PostMapping("/{documentId}/review")
    public Decision submitManualReview(
            @PathVariable Long documentId,
            @RequestParam String manualComment,
            HttpSession session
    )

    {
        User reviewer = (User) session.getAttribute("USER");
        if (reviewer == null) {
            throw new IllegalStateException("User not logged in");
        }

        if (manualComment == null || manualComment.isBlank()) {
            throw new IllegalArgumentException("Manual comment cannot be empty");
        }

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Document not found")
                );

        Decision decision = decisionRepository.findByDocument(document)
                .orElseThrow(() ->
                        new EntityNotFoundException("Decision not found")
                );

        decision.setDecisionResult(DecisionResult.MANUAL_REVIEW.name());
        decision.setManualComment(manualComment);
        decision.setReviewedByUserId(reviewer.getId());
        decision.setReviewedByRole(reviewer.getRole());
        decision.setFinalizedAt(LocalDateTime.now());

        return decisionRepository.save(decision);
    }
}
