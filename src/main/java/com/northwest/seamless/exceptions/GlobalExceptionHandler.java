package com.northwest.seamless.exceptions;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.northwest.seamless.dtos.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("NullableProblems")
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

     @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception ex) {

        log.error("Unexpected error", ex);

        ApiResponse<String> response = ApiResponse.error(ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

        @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(
            MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.error("Validation error: {}", errorMessage, ex);

        ApiResponse<String> apiResponse = ApiResponse.error(errorMessage);

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

       @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentials(BadCredentialsException ex) {

        ApiResponse<String> response = ApiResponse.error("Invalid Username or Password");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

      @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiResponse<String>> handleApplicationException(final ApplicationException ex) {

        log.error("Application error: {}", ex.getMessage(), ex);
        ApiResponse<String> apiResponse = new ApiResponse<>(false, ex.getMessage(), ex.getMessage());

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

}
