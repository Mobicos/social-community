package org.example.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    @Pointcut("@annotation(org.example.model.annotation.LoggingMonitor)")
    public void serviceMethods() {
        // 空实现，不需要任何逻辑
    }

    @Before("serviceMethods()")
    public void logBefore() {
        System.out.println("Method is about to be executed");
    }

    @After("serviceMethods()")
    public void logAfter() {
        System.out.println("Method has been executed");
    }
}
