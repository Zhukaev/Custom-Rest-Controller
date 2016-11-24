package com.qiwi.user.exeption;

import com.qiwi.servlet.annotation.exception.QiwiExceptionHandler;
import com.qiwi.servlet.annotation.exception.QiwiGlobalExceptionHandler;
import org.eclipse.jetty.http.HttpStatus;

@QiwiGlobalExceptionHandler
public class ExceptionHandler {
    @QiwiExceptionHandler(value = NotFoundUserException.class, httpStatus = HttpStatus.NOT_FOUND_404)
    public String handleNotFound(Exception e) {
        return "Пользователь не найден";
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
    public String handleException(Exception e) {
        return "Internal server error";
    }
}
