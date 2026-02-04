package com.example.DB.Repositories.DecisionRepository;

import com.example.DB.Models.DecisionModel.Decision;
import com.example.DB.Models.DocumentModel.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DecisionRepository extends JpaRepository<Decision, Long> {

    Optional<Decision> findByDocument(Document document);

    List<Decision> findTop10ByOrderByFinalizedAtDesc();

    long countByDecisionResult(String decisionResult);
}
