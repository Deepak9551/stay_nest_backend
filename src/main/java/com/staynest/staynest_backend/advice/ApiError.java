package com.staynest.staynest_backend.advice;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(
        Map<String, String> fieldErrors,
        String errorCode,
        LocalDateTime time,
        String message
) {
   ApiError(
            Map<String, String> fieldErrors,
            String errorCode ,
            String message
    ) {
        this(fieldErrors, errorCode, LocalDateTime.now(),message);
    }

    ApiError(String errorCode, String message){
        this(null, errorCode, LocalDateTime.now(),message);
    }
}
