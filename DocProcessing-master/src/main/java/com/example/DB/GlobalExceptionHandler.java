package com.example.DB;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---------------- AUTH / SESSION ----------------

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleUserNotLoggedIn(IllegalStateException ex) {
        return build(HttpStatus.UNAUTHORIZED, "AUTH_REQUIRED", ex.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<?> handleAccessDenied(SecurityException ex) {
        return build(HttpStatus.FORBIDDEN, "ACCESS_DENIED", ex.getMessage());
    }

    // ---------------- AUTH / LOGIN ----------------

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleInvalidInput(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, "INVALID_INPUT", ex.getMessage());
    }

    // ---------------- FILE UPLOAD ----------------

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> handleMultipartError(MultipartException ex) {
        return build(
                HttpStatus.BAD_REQUEST,
                "INVALID_MULTIPART_REQUEST",
                "Request must be multipart/form-data"
        );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleFileTooLarge(MaxUploadSizeExceededException ex) {
        return build(
                HttpStatus.PAYLOAD_TOO_LARGE,
                "FILE_TOO_LARGE",
                "Uploaded file exceeds allowed size"
        );
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleFileIO(IOException ex) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "FILE_IO_ERROR",
                "File processing failed"
        );
    }

    // ---------------- VALIDATION ----------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        return build(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_FAILED",
                "Request validation failed"
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParam(MissingServletRequestParameterException ex) {
        return build(
                HttpStatus.BAD_REQUEST,
                "MISSING_PARAMETER",
                ex.getParameterName() + " is required"
        );
    }

    // ---------------- DATABASE ----------------

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex) {
        return build(
                HttpStatus.NOT_FOUND,
                "RESOURCE_NOT_FOUND",
                ex.getMessage()
        );
    }

    // ---------------- DOCUMENT / DECISION ----------------

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<?> handleUnsupportedOperation(UnsupportedOperationException ex) {
        return build(
                HttpStatus.CONFLICT,
                "OPERATION_NOT_ALLOWED",
                ex.getMessage()
        );
    }

    // ---------------- DASHBOARD / AUDIT ----------------

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointer(NullPointerException ex) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "NULL_REFERENCE",
                "Unexpected null value encountered"
        );
    }

    // ---------------- GENERIC RUNTIME ----------------

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        return build(
                HttpStatus.BAD_REQUEST,
                "OPERATION_FAILED",
                ex.getMessage()
        );
    }

    // ---------------- FALLBACK (LAST LINE OF DEFENSE) ----------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "An unexpected server error occurred"
        );
    }

    // ---------------- RESPONSE BUILDER ----------------

    private ResponseEntity<Map<String, Object>> build(
            HttpStatus status,
            String error,
            String message
    ) {
        return ResponseEntity
                .status(status)
                .body(Map.of(
                        "error", error,
                        "message", message,
                        "timestamp", LocalDateTime.now()
                ));
    }
}
