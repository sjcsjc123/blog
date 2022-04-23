package com.example.myblog.exception.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class InterceptorAop {

    @Pointcut("execution(* com.example.myblog.interceptor.*.*(..))")
    public void aop(){

    }

    @Before("aop()")// 拦截被TestAnnotation注解的方法；如果你需要拦截指定package
    // 指定规则名称的方法，可以使用表达式execution(...)，具体百度一下资料一大堆
    public void beforeTest(JoinPoint point) throws Throwable {
    }

    @AfterThrowing(value = "aop()",throwing = "throwable")
    public void afterThrowing(Throwable throwable){
        throwable.printStackTrace();
    }

}
