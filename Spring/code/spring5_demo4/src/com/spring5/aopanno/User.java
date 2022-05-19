package com.spring5.aopanno;


import org.springframework.stereotype.Component;

@Component
public class User {

    public void add(){
//        int i = 1/0;
        System.out.println("add 业务逻辑执行.....");
    }

}
