package com.example.DB.Services.DocumentProcessingService;

import org.springframework.stereotype.Service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

@Service
public class OcrService {

    public String text;
    private final ITesseract tesseract;
    private final ExtractionService extractionService;

    public OcrService(ExtractionService extractionService) {
        this.extractionService = extractionService;

        tesseract = new Tesseract();

        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");

        tesseract.setLanguage("eng");
    }

    public String extractTextFromImage(File imageFile) throws TesseractException {

        if (imageFile == null) {
            throw new TesseractException("Unsupported image format");
        }

        text = tesseract.doOCR(imageFile);
        return text;
    }
}

