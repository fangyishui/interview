package com.spring5.aopanno;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//增强的类
@Aspect //生成代理对象
@Component
@Order(2)
public class UserProxy {

    //相同切入点抽取
    @Pointcut("execution(* com.spring5.aopanno.User.add(..))")
    public void pointTest(){

    }
    //前置通知
    @Before("pointTest()")//前置通知
    public void before(){
        System.out.println("before-前置通知-......");
    }

    //后置通知（返回通知）-方法执行后-返回结果之后执行
    @AfterReturning("execution(* com.spring5.aopanno.User.add(..))")
    public void afterReturning(){
        System.out.println("afterReturning-后置通知（返回通知）-......");
    }


    //最终通知-方法执行之后执行
    @After("execution(* com.spring5.aopanno.User.add(..))")
    public void after(){
        System.out.println("after-最终通知......");
    }


    //异常通知
    @AfterThrowing("execution(* com.spring5.aopanno.User.add(..))")
    public void afterThrowing(){
        System.out.println("afterThrowing-异常通知......");
    }


    //环绕通知
    @Around("execution(* com.spring5.aopanno.User.add(..))")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("around-环绕之前通知-......");
        joinPoint.proceed();
        System.out.println("around-环绕之后通知-......");
    }

}
