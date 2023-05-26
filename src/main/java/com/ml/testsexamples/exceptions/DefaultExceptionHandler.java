package com.ml.testsexamples.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        log.error("Unhandled exception occurred. ", e);
        ApiError apiError = new ApiError(request.getRequestURI(), e.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(InactiveAccountException.class)
    public ResponseEntity<ApiError> handleInactiveAccountException(InactiveAccountException e, HttpServletRequest request) {
        log.error("Unhandled exception occurred. ", e);
        ApiError apiError = new ApiError(request.getRequestURI(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ApiError> handleInsufficientFundsException(InsufficientFundsException e, HttpServletRequest request) {
        log.error("Unhandled exception occurred. ", e);
        ApiError apiError = new ApiError(request.getRequestURI(), e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("Unhandled exception occurred. ", e);
        ApiError apiError = new ApiError(request.getRequestURI(), e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(EmailValidationException.class)
    public ResponseEntity<ApiError> handleEmailValidationException(EmailValidationException e, HttpServletRequest request) {
        log.error("Unhandled exception occurred. ", e);
        ApiError apiError = new ApiError(request.getRequestURI(), e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(AmountValidationException.class)
    public ResponseEntity<ApiError> handleAmountValidationException(AmountValidationException e, HttpServletRequest request) {
        log.error("Unhandled exception occurred. ", e);
        ApiError apiError = new ApiError(request.getRequestURI(), e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleExceptions(Exception e, HttpServletRequest request) {
        log.error("Unhandled exception occurred", e);
        ApiError apiError = new ApiError(request.getRequestURI(), e.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
}
