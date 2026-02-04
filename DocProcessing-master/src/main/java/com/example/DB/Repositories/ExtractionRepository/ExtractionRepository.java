package com.example.DB.Repositories.ExtractionRepository;

import com.example.DB.Models.ExtractionModel.Extraction;
import com.example.DB.Models.DocumentModel.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExtractionRepository extends JpaRepository<Extraction, Long> {

    Optional<Extraction> findByDocument(Document document);
}
