package com.qiwi.servlet.exception;

import java.lang.reflect.Method;

public class HandlerDefinition {

    private Method method;
    private int httpStatus;
    private Object objHandler;

    public HandlerDefinition(Method method, int httpStatus, Object objHandler) {
        this.method = method;
        this.httpStatus = httpStatus;
        this.objHandler = objHandler;
    }

    public Method getMethod() {
        return method;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public Object getObjHandler() {
        return objHandler;
    }
}
