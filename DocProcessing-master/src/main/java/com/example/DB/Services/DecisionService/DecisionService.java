package com.example.DB.Services.DecisionService;

import com.example.DB.Enums.DecisionResult;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DecisionService {
    public int calculateConfidenceScore(Map<String, String> fields) {

        int score = 0;

        for (Map.Entry<String, String> entry : fields.entrySet()) {

            String label = entry.getKey();
            String value = entry.getValue();

            // Strong identifier (certificate numbers, IDs)
            if (value.matches(".*\\d{6,}.*")) {
                score += 15;
            }

            // Date presence
            if (value.matches(".*\\d{2}[-/]\\d{2}[-/]\\d{4}.*")) {
                score += 10;
            }

            // Bilingual labels (govt style)
            if (label.contains("/") || label.matches(".*[\\p{InTamil}\\p{InDevanagari}].*")) {
                score += 10;
            }

            // Good value length
            if (value.length() >= 5 && value.length() <= 100) {
                score += 5;
            }

            // Penalize noisy labels
            if (label.length() < 4) {
                score -= 10;
            }
        }

        // Bonus for completeness
        if (fields.size() >= 4) {
            score += 10;
        }

        return Math.min(score, 100); // cap at 100
    }

    public DecisionResult makeDecision(Map<String, String> fields) {

        int confidence = calculateConfidenceScore(fields);

        if (confidence >= 60) {
            return DecisionResult.AUTO_APPROVED;
        } else {
            return DecisionResult.MANUAL_REVIEW;
        }
    }

}
