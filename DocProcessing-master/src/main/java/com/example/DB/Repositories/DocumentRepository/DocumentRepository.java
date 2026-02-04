package com.example.DB.Repositories.DocumentRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.DB.Models.DocumentModel.Document;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findTop10ByOrderByUploadedAtDesc();

    long count();
}

