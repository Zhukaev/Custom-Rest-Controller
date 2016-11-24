package com.qiwi.servlet.requestmapping;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class RequestMappingImpl implements RequestMapping {

    private Pattern path;
    private Map<String, MethodDefinition> methodMap;

    public RequestMappingImpl(String pathRegex) {
        this.path = Pattern.compile(pathRegex);
        this.methodMap = new ConcurrentHashMap<String, MethodDefinition>();
    }

    @Override
    public Pattern getPath() {
        return path;
    }

    @Override
    public Map<String, MethodDefinition> getMethodMap() {
        return methodMap;
    }

    public void addMethod(String httpMethodName, MethodDefinition methodDefinition) {
        methodMap.put(httpMethodName, methodDefinition);
    }
}
