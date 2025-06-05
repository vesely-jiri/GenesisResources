package cz.jpcz.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, String>> buildErrorResponse(String error, String details, HttpStatus status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", error);
        errorResponse.put("details", details);
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
        log.error("UserNotFoundException caught: {}", ex.getMessage());
        return buildErrorResponse("User not found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePersonNotFound(PersonNotFoundException ex) {
        log.error("PersonNotFoundException caught: {}", ex.getMessage());
        return buildErrorResponse("Person not found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PersonAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handlePersonAlreadyExists(PersonAlreadyExistsException ex) {
        log.error("PersonAlreadyExistsException caught: {}", ex.getMessage());
        return buildErrorResponse("Person already exists", ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("DataIntegrityViolationException caught: {}", ex.getMessage());
        return buildErrorResponse("Data integrity violation", ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        log.error("Generic exception caught: {}", ex.getMessage());
        return buildErrorResponse("Unexpected error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
