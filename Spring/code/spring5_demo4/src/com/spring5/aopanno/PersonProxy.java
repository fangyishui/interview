package com.spring5.aopanno;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class PersonProxy {

    @Before("execution(* com.spring5.aopanno.User.add(..))")
    public void before(){
        System.out.println("person before .....");
    }
}
