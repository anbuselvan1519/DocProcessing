package com.example.DB.DTO;

import com.example.DB.Enums.DecisionResult;

import java.util.Map;

public record WorkflowResult(Map<String, String> extractedFields, String extractedText, int confidenceScore, DecisionResult decision) {

}
