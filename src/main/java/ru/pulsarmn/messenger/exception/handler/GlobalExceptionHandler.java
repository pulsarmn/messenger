package ru.pulsarmn.messenger.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.pulsarmn.messenger.dto.ErrorResponse;
import ru.pulsarmn.messenger.exception.BadCredentialsException;
import ru.pulsarmn.messenger.exception.UserAlreadyExistsException;
import ru.pulsarmn.messenger.exception.UserNotFoundException;

import java.time.Clock;
import java.time.Instant;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Clock clock;

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler(Clock clock) {
        this.clock = clock;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    ResponseEntity<ErrorResponse> handleUserAlreadyExists(HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "Username is already taken", request);
    }

    @ExceptionHandler({BadCredentialsException.class, UserNotFoundException.class})
    ResponseEntity<ErrorResponse> handleBadCredentials(HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Bad credentials", request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid body argument", request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ErrorResponse> handleDataIntegrityViolation(HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "Username is already taken", request);
    }


    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("Unexpected error occurred on path: {}", request.getRequestURI(), e);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status)
                .error(status.getReasonPhrase())
                .message(message)
                .timestamp(Instant.now(clock))
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status)
                .body(errorResponse);
    }
}
