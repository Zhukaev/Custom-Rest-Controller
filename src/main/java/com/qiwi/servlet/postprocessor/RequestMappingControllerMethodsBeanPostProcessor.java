package com.qiwi.servlet.postprocessor;

import com.qiwi.servlet.requestmapping.MethodDefinition;
import com.qiwi.servlet.requestmapping.RequestMappingService;
import com.qiwi.servlet.annotation.requestmapping.QiwiPathVariable;
import com.qiwi.servlet.annotation.requestmapping.QiwiRestController;
import com.qiwi.servlet.annotation.requestmapping.QiwiRequestBody;
import com.qiwi.servlet.annotation.requestmapping.QiwiRequestMapping;
import com.qiwi.servlet.requestparam.PathVariable;
import com.qiwi.servlet.requestparam.RequestBody;
import com.qiwi.servlet.requestparam.RequestParam;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class RequestMappingControllerMethodsBeanPostProcessor implements BeanPostProcessor {

    private Map<String, Object> originalBean = new HashMap<String, Object>();
    private final RequestMappingService requestMappingService;

    @Autowired
    public RequestMappingControllerMethodsBeanPostProcessor(RequestMappingService requestMappingService) {
        this.requestMappingService = requestMappingService;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().getAnnotation(QiwiRestController.class) != null) {
            this.originalBean.put(beanName, bean);

            /*Method[] methods = bean.getClass().getDeclaredMethods();

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
                MethodDefinition newMethodDefinition = new MethodDefinition(this.originalBean, this.method, params);
                requestMappingService.addMethodDefinition(newMethodDefinition, this.path, this.httpMethod);
            }*/
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object proxy, String beanName) throws BeansException {
        Object object = originalBean.get(beanName);
        if (object == null) {
            return proxy;
        }

        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method beanMethod : methods) {

            List<RequestParam> params = getRequestParams(beanMethod);

            String path = null;
            String httpMethod = null;
            QiwiRequestMapping annotationQiwiRequestMapping = beanMethod.getAnnotation(QiwiRequestMapping.class);
            if (annotationQiwiRequestMapping != null) {
                httpMethod = annotationQiwiRequestMapping.method();
                path = annotationQiwiRequestMapping.path();
            }

            Method proxyMethod = ReflectionUtils.findMethod(proxy.getClass(), beanMethod.getName(), beanMethod.getParameterTypes());

            MethodDefinition newMethodDefinition = new MethodDefinition(proxy, proxyMethod, params);
            requestMappingService.addMethodDefinition(newMethodDefinition, path, httpMethod);
        }

        return proxy;
    }

    private List<RequestParam> getRequestParams(Method beanMethod) {
        List<RequestParam> params = new ArrayList<RequestParam>();
        for (Parameter parameter : beanMethod.getParameters()) {
            if (parameter.isAnnotationPresent(QiwiPathVariable.class)) {
                params.add(new PathVariable(parameter.getType()));
            } else if (parameter.isAnnotationPresent(QiwiRequestBody.class)) {
                params.add(new RequestBody(parameter.getType()));
            }
        }
        return params;
    }
}
