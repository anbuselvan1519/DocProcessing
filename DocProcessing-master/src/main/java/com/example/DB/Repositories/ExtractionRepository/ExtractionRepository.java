package com.example.DB.Repositories.ExtractionRepository;

import com.example.DB.Models.ExtractionModel.Extraction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExtractionRepository extends JpaRepository<Extraction, Long> {
}
