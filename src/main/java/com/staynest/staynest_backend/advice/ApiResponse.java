package com.staynest.staynest_backend.advice;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ApiResponse<T> {

    private LocalDateTime timeStamp;
    private ApiError apiError;
    private T data;

    public ApiResponse(){
        this.timeStamp = LocalDateTime.now();
    }

    public ApiResponse(ApiError apiError) {
        this();
        this.apiError = apiError;
    }
    public ApiResponse(T data) {
        this();
        this.data = data;
    }
}
