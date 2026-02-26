package org.ssnardo.receipt_ocr.common.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.ssnardo.receipt_ocr.common.dto.ApiError;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ MethodArgumentNotValidException.class, ConstraintViolationException.class,
            IllegalArgumentException.class })
    public ResponseEntity<ApiError> handleBadRequest(Exception ex, HttpServletRequest request) {

        logger.warn("Client error: {}", ex.getMessage());
        String message = ex instanceof MethodArgumentNotValidException
                ? ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors().stream()
                        .map(err -> err.getField() + ": " + err.getDefaultMessage())
                        .collect(Collectors.joining(", "))
                : ex.getMessage();
        ApiError error = new ApiError(LocalDateTime.now(), 400, "BAD_REQUEST", message, request.getRequestURI());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleBadJson(HttpMessageNotReadableException ex, HttpServletRequest request) {

        logger.warn("Client error: {}", ex.getMessage());
        String message = "Malformed JSON request or invalid request body";
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "BAD_REQUEST",
                message,
                request.getRequestURI());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<ApiError> handleUnauthenticated(
            UnauthenticatedException ex,
            HttpServletRequest request) {

        logger.warn("Client error: {}", ex.getMessage());
        ApiError error = new ApiError(
                LocalDateTime.now(),
                401,
                "UNAUTHORIZED",
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {

        logger.warn("Client error: {}", ex.getMessage());
        ApiError error = new ApiError(LocalDateTime.now(), 403, "ACCESS_DENIED",
                "You do not have permission to access this resource", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {

        logger.warn("Client error: {}", ex.getMessage());
        ApiError error = new ApiError(LocalDateTime.now(), 404, "RESOURCE_NOT_FOUND", ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {

        logger.warn("Client error: {}", ex.getMessage());
        String message = "HTTP method " + ex.getMethod() + " is not supported for this endpoint.";
        ApiError error = new ApiError(
                LocalDateTime.now(),
                405,
                "METHOD_NOT_ALLOWED",
                message,
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleConflict(ResourceAlreadyExistsException ex, HttpServletRequest request) {

        logger.warn("Client error: {}", ex.getMessage());
        ApiError error = new ApiError(LocalDateTime.now(), 409, "RESOURCE_ALREADY_EXISTS", ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleUnsupportedMedia(
            HttpMediaTypeNotSupportedException ex,
            HttpServletRequest request) {

        logger.warn("Client error: {}", ex.getMessage());
        String message = "Media type " + ex.getContentType() + " is not supported.";
        ApiError error = new ApiError(
                LocalDateTime.now(),
                415,
                "UNSUPPORTED_MEDIA_TYPE",
                message,
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneral(Exception ex, HttpServletRequest request) {

        logger.error("Unhandled exception", ex);
        ApiError error = new ApiError(LocalDateTime.now(), 500, "INTERNAL_SERVER_ERROR", "An unexpected error occurred",
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
