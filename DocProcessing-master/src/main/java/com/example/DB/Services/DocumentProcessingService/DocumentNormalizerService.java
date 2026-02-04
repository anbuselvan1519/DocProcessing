package com.example.DB.Services.DocumentProcessingService;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentNormalizerService {

    public List<File> normalizeToImages(File inputFile) throws IOException {

        String fileName = inputFile.getName().toLowerCase();
        List<File> imageFiles = new ArrayList<>();

        if (isImage(fileName)) {
            imageFiles.add(inputFile);
            return imageFiles;
        }

        if (fileName.endsWith(".pdf")) {
            return convertPdfToImages(inputFile);
        }

        throw new IOException("Unsupported file type: " + fileName);
    }

    private boolean isImage(String fileName) {
        return fileName.endsWith(".jpg") ||
                fileName.endsWith(".jpeg") ||
                fileName.endsWith(".png") ||
                fileName.endsWith(".tiff");
    }

    private List<File> convertPdfToImages(File pdfFile) throws IOException {

        List<File> imageFiles = new ArrayList<>();

        try (PDDocument document = Loader.loadPDF(pdfFile)) {

            PDFRenderer renderer = new PDFRenderer(document);

            for (int page = 0; page < document.getNumberOfPages(); page++) {

                BufferedImage image =
                        renderer.renderImageWithDPI(page, 300);

                File outputImage = new File(
                        pdfFile.getParent(),
                        pdfFile.getName() + "_page_" + page + ".jpg"
                );

                ImageIO.write(image, "jpg", outputImage);
                imageFiles.add(outputImage);
            }
        }

        return imageFiles;
    }
}
