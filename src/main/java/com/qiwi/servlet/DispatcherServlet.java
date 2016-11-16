package com.qiwi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiwi.appwithcustomrestcontroller.exeption.UserException;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DispatcherServlet extends HttpServlet {

    private final ConversionService conversionService;
    private final List<RequestMapping> methods;

    @Autowired
    public DispatcherServlet(ConversionService conversionService, List<RequestMapping> methods) {
        this.conversionService = conversionService;
        this.methods = methods;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String methodName = request.getMethod();
        String pathInfo = request.getPathInfo();
        String contextPath = request.getContextPath();
        String path = contextPath + pathInfo;
        RequestMapping requestMapping = getRequestMapping(path);
        MethodDefinition methodDefinition = requestMapping.getMethodMap().get(methodName);
        Method invokeMethod = methodDefinition.getMethod();
        Object args[] = getParam(requestMapping, methodDefinition, request);
        try {
            Object obj = invokeMethod.invoke(methodDefinition.getObject(), args);
            setObjectToResponse(obj, HttpStatus.OK_200, response);
        } catch (Exception e) {

            setObjectToResponse("Internal server error",HttpStatus.INTERNAL_SERVER_ERROR_500, response);
        }
    }

    public RequestMapping getRequestMapping(String path) {
        for (RequestMapping methodDefine : methods) {
            Matcher matcher = methodDefine.getPath().matcher(path);
            if (matcher.matches()) {
                return methodDefine;
            }
        }
        return null;
    }

    /*public MethodDefinition getMethodDefininition(String path, String httpMethod) {
        for (RequestMapping methodDefine : methods) {
            Matcher matcher = methodDefine.getPath().matcher(path);
            if (matcher.matches()) {
                return methodDefine.getMethodMap().get(httpMethod);
            }
        }
        return null;
    }*/

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

    public Object[] getParam(RequestMapping requestMapping, MethodDefinition methodDefinition, HttpServletRequest request) throws ServletException, IOException {
        if (methodDefinition.getParams() == null) return null;
        Object[] argsArray = new Object[methodDefinition.getParams().size()];
        int i = 0;
        for (RequestParam param : methodDefinition.getParams()) {
            if (param.getClass().equals(PathVariable.class)) {
                argsArray[i] = getURLParam(param.getClazz(), requestMapping, request);
                i++;
            }
            else if (param.getClass().equals(RequestBody.class)){
                argsArray[i] = getBody(request, param.getClazz());
                i++;
            }
        }
        return argsArray;
    }

    private Object getURLParam(Class className, RequestMapping requestMapping, HttpServletRequest request) {
        Pattern pattern = requestMapping.getPath();
        Matcher matcher = pattern.matcher(request.getPathInfo());
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
        response.setContentType("application/json");
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