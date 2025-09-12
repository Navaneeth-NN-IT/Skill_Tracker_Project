package com.example.skill_tracker_project.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice // This annotation makes this class a global exception handler
public class GlobalExceptionHandler {

    // Handle Resource Not Found exceptions (already exists in your code)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // --- NEW EXCEPTION HANDLER: For all RuntimeExceptions (including the duplicate ones) ---
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST); // Return 400
    }

    // --- Helper class for error details ---
    private static class ErrorDetails {
        private Date timestamp;
        private String message;
        private String details;

        public ErrorDetails(Date timestamp, String message, String details) {
            this.timestamp = timestamp;
            this.message = message;
            this.details = details;
        }

        // Getters for serialization
        public Date getTimestamp() { return timestamp; }
        public String getMessage() { return message; }
        public String getDetails() { return details; }
    }
}