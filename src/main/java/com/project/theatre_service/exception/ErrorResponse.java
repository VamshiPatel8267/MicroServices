package com.project.theatre_service.exception;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public record ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(LocalDateTime.now(ZoneOffset.UTC), status, error, message, path);
    }
}
