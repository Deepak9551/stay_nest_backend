package com.staynest.staynest_backend.exception;

import com.staynest.staynest_backend.advice.ErrorCode;
import lombok.Getter;

@Getter
public class UnAuthorizedException extends RuntimeException {
    private ErrorCode errorCode;
    public UnAuthorizedException(String message,ErrorCode errorCode) {

        super(message);
        this.errorCode = errorCode;
    }
}
