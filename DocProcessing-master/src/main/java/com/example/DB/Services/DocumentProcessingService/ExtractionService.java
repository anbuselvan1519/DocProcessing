package com.example.DB.Services.DocumentProcessingService;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ExtractionService {

    public Map<String, String> extractFields(String ocrText) {

        Map<String, String> extractedFields = new HashMap<>();

        String[] lines = ocrText.split("\\r?\\n");

        for (int i = 0; i < lines.length; i++) {

            String line = normalizeLine(lines[i]);

            if (line.length() < 3) {
                continue;
            }

            if (containsLabelSeparator(line)) {

                String[] parts = splitLabelValue(line);

                String label = parts[0].trim();
                String value = parts[1].trim();

                if (value.isEmpty() && i + 1 < lines.length) {
                    String nextLine = normalizeLine(lines[i + 1]);
                    if (isValidValue(nextLine) && !containsLabelSeparator(nextLine)) {
                        value = nextLine;
                    }
                }

                value = cleanValue(value);

                if (isValidLabel(label) && isValidValue(value)) {
                    extractedFields.put(label, value);
                }
            }
        }

        return extractedFields;
    }

    private String normalizeLine(String line) {
        return line
                .replaceAll("\\p{C}", "")
                .replaceAll("：", ":")
                .replaceAll("\\s*:\\s*", ":")
                .trim();
    }

    private boolean containsLabelSeparator(String line) {
        return line.contains(":");
    }

    private String[] splitLabelValue(String line) {
        String[] parts = line.split(":", 2);
        if (parts.length == 1) {
            return new String[]{parts[0], ""};
        }
        return parts;
    }

    private String cleanValue(String value) {

        Pattern idPattern = Pattern.compile("[A-Z]{2}-\\d{8,}");
        Matcher idMatcher = idPattern.matcher(value);
        if (idMatcher.find()) {
            return idMatcher.group();
        }

        Pattern datePattern = Pattern.compile("\\d{2}[-/]\\d{2}[-/]\\d{4}");
        Matcher dateMatcher = datePattern.matcher(value);
        if (dateMatcher.find()) {
            return dateMatcher.group();
        }

        return value;
    }

    private boolean isValidLabel(String label) {
        return label.length() <= 50 && !label.matches(".*\\d{4,}.*");
    }

    private boolean isValidValue(String value) {
        return value.length() >= 2 && value.length() <= 150;
    }
}
