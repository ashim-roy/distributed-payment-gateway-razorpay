package com.AshimCS.razorpay.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String errorCode,
        String errorDescription,
        LocalDateTime timestamp,
        List<FieldError> fieldErrors
) {
    public record FieldError(String field, String message) { }

    public static ErrorResponse of(String errorCode, String errorDescription) {
        return new ErrorResponse(errorCode, errorDescription, LocalDateTime.now(), null);
    }
/*
The of methods are static factory methods. Instead of forcing you to write a long constructor call everywhere in your exception handler—where
you'd have to manually type LocalDateTime.now() and pass null for missing fields—the of methods act as clean, readable shortcuts.
 */


    public static ErrorResponse of(String errorCode, String errorDescription, List<FieldError> fieldErrors) {
        return new ErrorResponse(errorCode, errorDescription, LocalDateTime.now(), fieldErrors);
    }
}

/*
When to use: When a client sends bad data in a request body (like an invalid email or a blank password) and Spring throws a validation exception.
 */

/**
 * Factory method to create an ErrorResponse with field errors.
 *
 * @param errorCode        The error code representing the type of error.
 * @param errorDescription A description of the error.
 * @param fieldErrors      A list of field errors associated with the request validation.
 * @return An instance of ErrorResponse containing the provided information.
 */