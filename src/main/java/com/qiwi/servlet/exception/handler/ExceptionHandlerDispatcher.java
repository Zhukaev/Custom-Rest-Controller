package com.qiwi.servlet.exception.handler;

import com.qiwi.servlet.exception.HandlerDefinition;
import com.qiwi.servlet.exception.ResponseHttpError;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Configuration
//@QiwiGlobalExceptionHandler
public class ExceptionHandlerDispatcher {

    @Autowired
    private ExceptionHandlerMapping handlers;

    public ResponseHttpError handle(Exception exception) throws InvocationTargetException, IllegalAccessException {

        HandlerDefinition handler = handlers.getHandlers().get(exception.getClass());
        Method method = handler.getMethod();
        int httpStatus = handler.getHttpStatus();
        Object obj = handler.getObjHandler();
        if (method == null) {
            method = handlers.getHandlers().get(Exception.class).getMethod();
        }
        Object result = new ResponseHttpError(httpStatus, method.invoke(obj, exception));
        if (result instanceof ResponseHttpError) {
            return (ResponseHttpError) result;
        } else {
            return new ResponseHttpError(HttpStatus.INTERNAL_SERVER_ERROR_500, result);
        }
    }
}
