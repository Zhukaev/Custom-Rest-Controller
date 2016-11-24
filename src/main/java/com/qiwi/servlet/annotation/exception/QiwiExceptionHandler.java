package com.qiwi.servlet.annotation.exception;


import org.eclipse.jetty.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface QiwiExceptionHandler {

    Class<? extends Throwable> value();

    int httpStatus() default HttpStatus.INTERNAL_SERVER_ERROR_500;
}
