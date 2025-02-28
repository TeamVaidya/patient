package com.spring.vaidya.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.spring.vaidya.entity.ErrorResponse;

/**
 * Global exception handler for handling application-wide exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exceptions when a requested user is not found.
     * 
     * @param ex The UserNotFoundException.
     * @return ResponseEntity containing an error response with HTTP status 404 (NOT FOUND).
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "USER_NOT_FOUND",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exceptions when a user already exists in the system.
     * 
     * @param ex The UserAlreadyExistsException.
     * @return ResponseEntity containing an error response with HTTP status 409 (CONFLICT).
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "USER_ALREADY_EXISTS",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handles exceptions when a slot is already booked.
     * 
     * @param ex The SlotAlreadyBookedException.
     * @return ResponseEntity containing an error response with HTTP status 400 (BAD REQUEST).
     */
    @ExceptionHandler(SlotAlreadyBookedException.class)
    public ResponseEntity<Map<String, Object>> handleSlotAlreadyBookedException(SlotAlreadyBookedException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("errorCode", "SLOT_ALREADY_BOOKED");
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all unexpected exceptions that are not explicitly caught.
     * 
     * @param ex The general Exception.
     * @return ResponseEntity containing a generic error response with HTTP status 500 (INTERNAL SERVER ERROR).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
