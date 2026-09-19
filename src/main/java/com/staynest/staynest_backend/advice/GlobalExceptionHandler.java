package com.staynest.staynest_backend.advice;

import com.staynest.staynest_backend.exception.ResourceNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.staynest.staynest_backend.advice.ErrorCode.VALIDATION_ERROR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ApiResponse<?>> handle_resource_not_found(
            ResourceNotFound ex) {

        ApiError apiError = new ApiError(

                ex.getErrorCode().toString(),
                ex.getMessage()
        );

        return buildErrorResponse(apiError, NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handle_validation_exception(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        fieldErrors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ApiError apiError = new ApiError(
                VALIDATION_ERROR.toString(),
                fieldErrors.toString()

        );

        return buildErrorResponse(apiError, BAD_REQUEST);
    }

    private ResponseEntity<ApiResponse<?>> buildErrorResponse(
            ApiError apiError,
            HttpStatus status) {

        ApiResponse<?> response = new ApiResponse<>(apiError);

        return ResponseEntity
                .status(status)
                .body(response);
    }
}
