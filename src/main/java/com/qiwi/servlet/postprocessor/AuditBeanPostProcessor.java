package com.qiwi.servlet.postprocessor;

import com.qiwi.servlet.AuditService;
import com.qiwi.servlet.AuditServiceImpl;
import com.qiwi.servlet.annotation.audit.Audit;

import javassist.Modifier;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AuditBeanPostProcessor implements BeanPostProcessor {

    private Map<String, Class> map = new HashMap<String, Class>();
    private AuditService audit;

    @Autowired
    public AuditBeanPostProcessor(AuditService audit) {
        this.audit = audit;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class beanClass = bean.getClass();
        if (beanClass.getAnnotation(Audit.class) != null) {
            map.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        Class beanClass = map.get(beanName);
        if (beanClass != null) {
            /*ProxyFactory factory = new ProxyFactory();
            factory.setSuperclass(beanClass);
            factory.setFilter(new MethodFilter() {
                @Override
                public boolean isHandled(Method method) {
                    return Modifier.isPublic(method.getModifiers());
                }
            });
            MethodHandler handler = new MethodHandler() {
                @Override
                public Object invoke(Object o, Method method, Method method1, Object[] objects) throws Throwable {
                    System.out.println("hello from " + method.getName());
                    return null;
                }
            };
            Object proxy = null;
            try {
                proxy = factory.create(new Class<?>[0], new Object[0], handler);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return proxy;*/
            Object proxy = Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    audit.addAudit(method.getName());
                    Object retVal = method.invoke(bean, args);
                    return retVal;
                }
            });
            return proxy;
        }
        return bean;
    }
}