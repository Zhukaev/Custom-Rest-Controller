package com.qiwi.servlet.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiwi.user.exeption.UserException;
import com.qiwi.servlet.requestmapping.MethodDefinition;
import com.qiwi.servlet.requestmapping.RequestMapping;
import com.qiwi.servlet.requestmapping.RequestMappingService;
import com.qiwi.servlet.exception.ResponseHttpError;
import com.qiwi.servlet.exception.handler.ExceptionHandlerDispatcher;
import com.qiwi.servlet.requestparam.PathVariable;
import com.qiwi.servlet.requestparam.RequestBody;
import com.qiwi.servlet.requestparam.RequestParam;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DispatcherServlet extends HttpServlet {

    private final ConversionService conversionService;
    private final RequestMappingService requestMappingService;
    private final ExceptionHandlerDispatcher exceptionHandlerDispatcher;

    @Autowired
    public DispatcherServlet(ConversionService conversionService, RequestMappingService requestMappingService, ExceptionHandlerDispatcher exceptionHandlerDispatcher) {
        this.conversionService = conversionService;
        this.requestMappingService = requestMappingService;
        this.exceptionHandlerDispatcher = exceptionHandlerDispatcher;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String methodName = request.getMethod();
        String pathInfo = request.getPathInfo();
        String contextPath = request.getContextPath();
        String path = contextPath + pathInfo;
        try {
            RequestMapping requestMapping = getRequestMapping(path);
            MethodDefinition methodDefinition = requestMapping.getMethodMap().get(methodName);
            Method invokeMethod = methodDefinition.getMethod();
            Object args[] = getArgsArr(requestMapping, methodDefinition, request);
            Object obj = invokeMethod.invoke(methodDefinition.getObject(), args);
            setObjectToResponse(obj, HttpStatus.OK_200, response);
        } catch (InvocationTargetException e){
            Exception ex = (Exception) e.getCause();
            exceptionHandler(response, ex);
        } catch (Exception e) {
            exceptionHandler(response, e);
        }
    }

    private void exceptionHandler(HttpServletResponse response, Exception e) throws IOException {
        ResponseHttpError obj = null;
        try {
            obj = exceptionHandlerDispatcher.handle(e);
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
        setObjectToResponse(obj.getResponseBody(), obj.getErrorCode(), response);
    }

    public RequestMapping getRequestMapping(String path) {
        return requestMappingService.getRequestMapping(path);
    }

    public Class[] getParamType(MethodDefinition methodDefinition) {
        if (methodDefinition.getParams() == null) return new Class[]{};
        Class[] paramTypeArray = new Class[methodDefinition.getParams().size()];
        int i = 0;
        for (RequestParam param : methodDefinition.getParams()) {
            paramTypeArray[i] = param.getClazz();
            i++;
        }
        return paramTypeArray;
    }

    public Object[] getArgsArr(RequestMapping requestMapping, MethodDefinition methodDefinition, HttpServletRequest request) throws ServletException, IOException {
        if (methodDefinition.getParams() == null) return null;
        Object[] argsArray = new Object[methodDefinition.getParams().size()];
        int i = 0;
        for (RequestParam param : methodDefinition.getParams()) {
            if (param.getClass().equals(PathVariable.class)) {
                argsArray[i] = getURLParam(param.getClazz(), requestMapping, request);
                i++;
            } else if (param.getClass().equals(RequestBody.class)) {
                argsArray[i] = getBody(request, param.getClazz());
                i++;
            }
        }
        return argsArray;
    }

    private Object getURLParam(Class className, RequestMapping requestMapping, HttpServletRequest request) {
        Pattern pattern = requestMapping.getPath();
        Matcher matcher = pattern.matcher(request.getPathInfo());
        matcher.matches();
        String param = matcher.group(1);
        Object obj = conversionService.convert(param, className);
        return obj;
    }

    private Object getBody(HttpServletRequest request, Class className) throws ServletException, IOException, UserException {
        StringBuilder jsonObj = new StringBuilder();
        BufferedReader reader = request.getReader();
        String str;
        ObjectMapper mapper = new ObjectMapper();
        while ((str = reader.readLine()) != null)
            jsonObj.append(str);
        Object object = mapper.readValue(jsonObj.toString(), className);
        reader.close();
        return object;
    }

    private void setObjectToResponse(Object object, int httpStatus, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(httpStatus);
        ObjectMapper mapper = new ObjectMapper();
        String jsonObject;
        jsonObject = mapper.writeValueAsString(object);
        PrintWriter out = response.getWriter();
        out.print(jsonObject);
        out.flush();
        out.close();
    }
}