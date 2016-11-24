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

    /*@QiwiExceptionHandler(NotFoundUserException.class)
    public ResponseHttpError handleNotFound(Exception e) {
        return new ResponseHttpError(HttpStatus.NOT_FOUND_404, "Пользователь не найден");
    }

    @QiwiExceptionHandler(value = DuplicatePnoneException.class, httpStatus = HttpStatus.UNPROCESSABLE_ENTITY_422)
    public String handleDuplicatePhone(Exception e) {
        return "Пользователь с таким номером уже зарегистрирован";
    }

    @QiwiExceptionHandler(value = DataValidationException.class, httpStatus = HttpStatus.UNPROCESSABLE_ENTITY_422)
    public String handleDataValidate(Exception e) {
        return "Ведены неверные данные";
    }

    @QiwiExceptionHandler(Exception.class)
    public ResponseHttpError handleException(Exception e) {
        return new ResponseHttpError(HttpStatus.INTERNAL_SERVER_ERROR_500, "Internal server error");
    }*/

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
