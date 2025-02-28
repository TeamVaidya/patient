package com.spring.vaidya.entity;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema; // OpenAPI annotations

@Schema(description = "Standard error response model for API exceptions")
public class ErrorResponse {

    @Schema(description = "Timestamp when the error occurred", example = "2024-02-24T12:34:56")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code of the error", example = "400")
    private int status;

    @Schema(description = "Application-specific error code", example = "ERR-INVALID-REQUEST")
    private String errorCode;

    @Schema(description = "Detailed error message", example = "Invalid input data")
    private String message;

    // ✅ Default constructor for Spring
    public ErrorResponse() {
    }

    // ✅ Parameterized constructor
    public ErrorResponse(LocalDateTime timestamp, int status, String errorCode, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    // ✅ Getters and Setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
