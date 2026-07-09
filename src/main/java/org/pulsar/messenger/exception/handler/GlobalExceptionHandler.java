package org.pulsar.messenger.exception.handler;


import jakarta.servlet.http.HttpServletRequest;
import org.pulsar.messenger.dto.ErrorResponse;
import org.pulsar.messenger.exception.BadCredentialsException;
import org.pulsar.messenger.exception.PasswordsMismatchException;
import org.pulsar.messenger.exception.UserAlreadyExistsException;
import org.pulsar.messenger.exception.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PasswordsMismatchException.class)
    ResponseEntity<ErrorResponse> handlePasswordsMismatch(HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Passwords mismatch", request);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    ResponseEntity<ErrorResponse> handleUserAlreadyExists(HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "Username is already taken", request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorResponse> handleUserNotFound(HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, "User not found", request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<ErrorResponse> handleBadCredentials(HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Bad credentials", request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Invalid body argument", request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ErrorResponse> handleDataIntegrityViolation(HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "Username is already taken", request);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
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
