package com.qiwi.servlet.postprocessor;

import com.qiwi.servlet.requestmapping.MethodDefinition;
import com.qiwi.servlet.requestmapping.RequestMappingService;
import com.qiwi.servlet.annotation.requestmapping.QiwiPathVariable;
import com.qiwi.servlet.annotation.requestmapping.QiwiRestController;
import com.qiwi.servlet.annotation.requestmapping.QiwiRequestBody;
import com.qiwi.servlet.annotation.requestmapping.QiwiRequestMapping;
import com.qiwi.servlet.requestparam.RequestParam;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RequestMappingControllerMethodsBeanPostProcessor implements BeanPostProcessor {

    private Object object;
    private Method method;
    private String httpMethod;
    private String path;
    private final RequestMappingService requestMappingService;

    @Autowired
    public RequestMappingControllerMethodsBeanPostProcessor(RequestMappingService requestMappingService) {
        this.requestMappingService = requestMappingService;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().getAnnotation(QiwiRestController.class) != null) {
            this.object = bean;

            Method[] methods = bean.getClass().getDeclaredMethods();

            for (Method method : methods) {
                this.method = method;
                List<RequestParam> params = new ArrayList<RequestParam>();

                QiwiRequestMapping annotationQiwiRequestMapping = method.getAnnotation(QiwiRequestMapping.class);
                if (annotationQiwiRequestMapping != null) {
                    this.httpMethod = annotationQiwiRequestMapping.method();
                    this.path = annotationQiwiRequestMapping.path();
                }
                Annotation[][] methodArgsAnnotations = method.getParameterAnnotations();
                Parameter[] parameters = method.getParameters();
                for (Parameter parameter : parameters) {
                    if (parameter.isAnnotationPresent(QiwiPathVariable.class)) {
                        params.add(new com.qiwi.servlet.requestparam.PathVariable(parameter.getType()));
                    } else if (parameter.isAnnotationPresent(QiwiRequestBody.class)) {
                        params.add(new com.qiwi.servlet.requestparam.RequestBody(parameter.getType()));
                    }
                }
                MethodDefinition newMethodDefinition = new MethodDefinition(this.object, this.method, params);
                requestMappingService.addMethodDefine(newMethodDefinition, this.path, this.httpMethod);
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
