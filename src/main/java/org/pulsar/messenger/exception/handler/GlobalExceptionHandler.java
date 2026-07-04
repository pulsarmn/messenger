package org.pulsar.messenger.exception.handler;


import jakarta.servlet.http.HttpServletRequest;
import org.pulsar.messenger.dto.ErrorResponse;
import org.pulsar.messenger.exception.PasswordsMismatchException;
import org.pulsar.messenger.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PasswordsMismatchException.class)
    public ResponseEntity<ErrorResponse> handlePasswordsMismatch(HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Passwords mismatch", request);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "User already exists", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", request);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status)
                .error(status.getReasonPhrase())
                .message(message)
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }
}
