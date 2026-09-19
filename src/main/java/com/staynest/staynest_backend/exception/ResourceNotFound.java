package com.staynest.staynest_backend.exception;

import com.staynest.staynest_backend.advice.ErrorCode;
import lombok.Getter;

public class ResourceNotFound extends RuntimeException {
    @Getter
    private ErrorCode errorCode;
    private String resourceName;
    private Object resourceId;
    public ResourceNotFound(String resourceName, Object resourceId , ErrorCode errorCode) {


        super(resourceName + "Not Found With resourceId: "+  resourceId );
        this.errorCode = errorCode;
    }

}
