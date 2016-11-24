package com.qiwi.servlet.exception.handler;

import com.qiwi.servlet.exception.HandlerDefinition;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExceptionHandlerMapping {

    private Map<Class, HandlerDefinition> handlers = new HashMap<Class, HandlerDefinition>();

    public Map<Class, HandlerDefinition> getHandlers() {
        return handlers;
    }

    public void addHandler(Class exceptionClass, HandlerDefinition method){
        handlers.put(exceptionClass, method);
    }
}
