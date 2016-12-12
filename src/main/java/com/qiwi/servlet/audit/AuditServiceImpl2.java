package com.qiwi.servlet.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Aspect
public class AuditServiceImpl2 {

    private List<String> audit;

    public AuditServiceImpl2() {
        this.audit = new ArrayList<String>();
    }

    public List<String> getAudit() {
        return audit;
    }

    @Around("@annotation(com.qiwi.servlet.annotation.audit.Audit)")
    public Object addAudit(ProceedingJoinPoint joinPoint) throws Throwable{
        Object retVal = joinPoint.proceed();
        this.audit.add(joinPoint.getSignature().getName());
        System.out.println(joinPoint.getSignature().getName());
        return retVal;
    }
}
