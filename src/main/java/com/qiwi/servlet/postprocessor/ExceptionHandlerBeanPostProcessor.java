package com.qiwi.servlet.postprocessor;

import com.qiwi.servlet.annotation.exception.QiwiExceptionHandler;
import com.qiwi.servlet.annotation.exception.QiwiGlobalExceptionHandler;
import com.qiwi.servlet.exception.handler.ExceptionHandlerMapping;
import com.qiwi.servlet.exception.HandlerDefinition;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Configuration
public class ExceptionHandlerBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    ExceptionHandlerMapping handlers;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        /*if (bean.getClass().getAnnotation(QiwiGlobalExceptionHandler.class) != null) {
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getAnnotation(QiwiExceptionHandler.class) != null){
                    int httpStatus = method.getAnnotation(QiwiExceptionHandler.class).httpStatus();
                    HandlerDefinition handler = new HandlerDefinition(method, httpStatus, bean);
                    handlers.addHandler(method.getAnnotation(QiwiExceptionHandler.class).value(), handler);
                }
            }
        }*/
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().getAnnotation(QiwiGlobalExceptionHandler.class) != null) {
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getAnnotation(QiwiExceptionHandler.class) != null){
                    int httpStatus = method.getAnnotation(QiwiExceptionHandler.class).httpStatus();
                    HandlerDefinition handler = new HandlerDefinition(method, httpStatus, bean);
                    handlers.addHandler(method.getAnnotation(QiwiExceptionHandler.class).value(), handler);
                }
            }
        }
        return bean;
    }
}
