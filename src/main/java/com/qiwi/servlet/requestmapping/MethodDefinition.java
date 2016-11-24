package com.qiwi.servlet.requestmapping;

import com.qiwi.servlet.requestparam.RequestParam;

import java.lang.reflect.Method;
import java.util.List;

public class MethodDefinition {

    private Object object;
    private Method methodName;
    private List<RequestParam> params;

    public MethodDefinition(Object object, Method methodName, List<RequestParam> params) {
        this.object = object;
        this.methodName = methodName;
        this.params = params;
    }

    public Object getObject() {
        return object;
    }

    public Method getMethod() {
        return methodName;
    }

    public List<RequestParam> getParams() {
        return params;
    }
}
