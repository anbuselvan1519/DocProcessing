package com.example.DB.Services.DocumentProcessingService;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

@Service
public class ImageProcessingService {

    public File preprocessImage(File originalImage) throws IOException {

        BufferedImage inputImage = ImageIO.read(originalImage);

        if (inputImage == null) {
            throw new IOException("Unsupported image format");
        }

        int newWidth = inputImage.getWidth() * 2;
        int newHeight = inputImage.getHeight() * 2;

        BufferedImage resizedImage = new BufferedImage(
                newWidth,
                newHeight,
                BufferedImage.TYPE_INT_RGB
        );

        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(inputImage, 0, 0, newWidth, newHeight, null);
        g.dispose();

        BufferedImage grayImage = new BufferedImage(
                resizedImage.getWidth(),
                resizedImage.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY
        );

        Graphics2D g2 = grayImage.createGraphics();
        g2.drawImage(resizedImage, 0, 0, null);
        g2.dispose();

        float[] sharpenKernel = {
                0f, -1f, 0f,
                -1f,  5f, -1f,
                0f, -1f, 0f
        };

        Kernel kernel = new Kernel(3, 3, sharpenKernel);
        ConvolveOp op = new ConvolveOp(kernel);
        BufferedImage sharpenedImage = op.filter(grayImage, null);

        File processedFile = new File(
                originalImage.getParent(),
                "processed_" + originalImage.getName().replaceAll("\\.(jpg|jpeg|png)$", ".png")
        );

        ImageIO.write(sharpenedImage, "png", processedFile);

        return processedFile;
    }
}
