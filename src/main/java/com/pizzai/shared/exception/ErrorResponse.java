package com.pizzai.shared.exception;

import java.time.OffsetDateTime;
import java.util.List;

public record ErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldErrorDetail> fieldErrors
) {

    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(OffsetDateTime.now(), status, error, message, path, List.of());
    }

    public static ErrorResponse withFields(int status, String error, String message, String path,
                                           List<FieldErrorDetail> fieldErrors) {
        return new ErrorResponse(OffsetDateTime.now(), status, error, message, path, fieldErrors);
    }

    public record FieldErrorDetail(String field, String message) {}
}
