package com.staynest.staynest_backend.advice;

import com.staynest.staynest_backend.exception.DuplicateResource;
import com.staynest.staynest_backend.exception.ResourceNotFound;
import com.staynest.staynest_backend.exception.UnAuthorizedException;
import io.jsonwebtoken.JwtException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.staynest.staynest_backend.advice.ErrorCode.VALIDATION_ERROR;
import static org.springframework.http.HttpStatus.*;

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

    @ExceptionHandler(DuplicateResource.class)
    public ResponseEntity<ApiResponse<?>> handle_duplicate_resource(DuplicateResource duplicateResource){
        ApiError apiError = new ApiError(
                duplicateResource.getErrorCode().toString(),
                duplicateResource.getMessage()
        );
        return buildErrorResponse(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ApiResponse<?>> handle_unauthorized_exception(UnAuthorizedException ex){
        ApiError apiError = new ApiError(
                ex.getErrorCode().toString(),
                ex.getMessage()
        );
        return buildErrorResponse(apiError, UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handle_access_denied(
            AccessDeniedException ex) {

        ApiError apiError = new ApiError(
                "FORBIDDEN",
                "You do not have permission to access this resource"
        );

        return buildErrorResponse(apiError, FORBIDDEN);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>> handle_jwt_exception(JwtException ex) {

        ApiError apiError = new ApiError(
                "UNAUTHORIZED",
                ex.getMessage()
        );

        return buildErrorResponse(apiError, UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handle_authentication_exception(AuthenticationException ex) {

        ApiError apiError = new ApiError(
                "UNAUTHENTICATED",
                ex.getMessage()
        );

        return buildErrorResponse(apiError, UNAUTHORIZED);
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
