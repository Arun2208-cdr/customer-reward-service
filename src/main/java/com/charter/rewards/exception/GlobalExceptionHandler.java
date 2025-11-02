package com.charter.rewards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link CustomerNotFoundException} and returns a 404 Not Found response.
     *
     * @param ex the exception thrown when a customer is not found
     * @return a {@link ResponseEntity} containing error details such as timestamp, status, and message
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCustomerNotFound(CustomerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    /**
     * Handles invalid date format errors that occur when parsing request parameters.
     * This exception is typically thrown when the provided date does not match the expected ISO format
     * (yyyy-MM-dd). For example, if the user passes "31-10-2025" instead of "2025-10-31".
     *
     * @param ex the {@link DateTimeParseException} thrown during date parsing
     * @return a {@link ResponseEntity} with HTTP 400 (Bad Request) status and an error message
     */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<?> handleInvalidDateFormat(DateTimeParseException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid date format. Use yyyy-MM-dd"));
    }

    /**
     * Handles illegal argument exceptions that occur due to invalid input values or parameters.
     * This may be triggered when custom validations fail — for example, when the start date
     * is after the end date or when other logical constraints are violated.
     *
     * @param ex the {@link IllegalArgumentException} thrown during request validation or processing
     * @return a {@link ResponseEntity} with HTTP 400 (Bad Request) status and a descriptive error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    /**
     * Handles all unexpected exceptions and returns a 500 Internal Server Error response.
     *
     * @param ex the exception thrown during request processing
     * @return a {@link ResponseEntity} containing error details such as timestamp, status, and message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", "Internal Server Error");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
