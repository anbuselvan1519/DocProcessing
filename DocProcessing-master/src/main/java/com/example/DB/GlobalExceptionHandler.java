package com.example.DB;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
@SuppressWarnings("unused") // Spring calls these via reflection
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

    // ---------------- INPUT ----------------

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
                ex.getMessage()
        );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleFileTooLarge(MaxUploadSizeExceededException ex) {
        return build(
                HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE,
                "FILE_TOO_LARGE",
                ex.getMessage()
        );
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleFileIO(IOException ex) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "FILE_IO_ERROR",
                ex.getMessage()
        );
    }

    // ---------------- VALIDATION ----------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        return build(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_FAILED",
                ex.getBindingResult().getFieldError() != null
                        ? ex.getBindingResult().getFieldError().getDefaultMessage()
                        : "Validation error"
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

    // ---------------- OPERATION ----------------

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<?> handleUnsupportedOperation(UnsupportedOperationException ex) {
        return build(
                HttpStatus.CONFLICT,
                "OPERATION_NOT_ALLOWED",
                ex.getMessage()
        );
    }

    // ---------------- RUNTIME ----------------

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        return build(
                HttpStatus.BAD_REQUEST,
                "OPERATION_FAILED",
                ex.getMessage()
        );
    }

    // ---------------- FALLBACK ----------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                ex.getMessage()
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
