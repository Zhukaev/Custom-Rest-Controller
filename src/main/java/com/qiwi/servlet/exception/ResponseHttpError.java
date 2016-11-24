package com.qiwi.servlet.exception;

public class ResponseHttpError {

    private int errorCode;
    private Object responseBody;

    public ResponseHttpError(int httpStatus, Object responseBody) {
        this.errorCode = httpStatus;
        this.responseBody = responseBody;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Object getResponseBody() {
        return responseBody;
    }
}
