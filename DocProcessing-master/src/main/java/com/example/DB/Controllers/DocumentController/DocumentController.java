package com.example.DB.Controllers.DocumentController;

import com.example.DB.DTO.WorkflowResult;
import com.example.DB.Models.DocumentModel.Document;
import com.example.DB.Models.UserModel.User;
import com.example.DB.Repositories.DocumentRepository.DocumentRepository;
import com.example.DB.Services.WorkFlowService.WorkFlowService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentRepository documentRepository;
    private final WorkFlowService workFlowService;

    public DocumentController(
            DocumentRepository documentRepository,
            WorkFlowService workflowService) {
        this.documentRepository = documentRepository;
        this.workFlowService = workflowService;
    }

    @PostMapping("/upload")
    public WorkflowResult uploadDocument(
            @RequestParam("file") MultipartFile file,
            HttpSession session
    ) throws IOException, TesseractException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        User user = (User) session.getAttribute("USER");
        if (user == null) {
            throw new IllegalStateException("User not logged in");
        }

        File tempFile = File.createTempFile(
                "doc_",
                "_" + file.getOriginalFilename()
        );
        file.transferTo(tempFile);

        Document document = new Document();
        document.setFileName(file.getOriginalFilename());
        document.setFilePath(tempFile.getAbsolutePath());
        document.setMimeType(file.getContentType());
        document.setStatus("UPLOADED");
        document.setUploadedAt(LocalDateTime.now());
        document.setUploadedByUserId(user.getId());
        document.setUploadedByRole(user.getRole());

        document = documentRepository.save(document);

        WorkflowResult result = workFlowService.processDocument(tempFile,user);

        document.setStatus("PROCESSED");
        documentRepository.save(document);

        return result;
    }

    @GetMapping("/{id}")
    public Document getDocument(@PathVariable Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Document not found")
                );
    }
}
