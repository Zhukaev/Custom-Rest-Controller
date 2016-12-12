/*
package com.qiwi.servlet.postprocessor;

import com.qiwi.servlet.audit.AuditService;
import com.qiwi.servlet.annotation.audit.Audit;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.annotation.Configuration;

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
*/
