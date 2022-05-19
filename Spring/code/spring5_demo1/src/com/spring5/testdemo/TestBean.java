package com.spring5.testdemo;

import com.spring5.Book;
import com.spring5.Orders;
import com.spring5.User;
import com.spring5.bean.Emp;
import com.spring5.service.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestBean {

    @Test
    public void test1(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean2.xml");
        UserService userService = applicationContext.getBean("userService", UserService.class);
       userService.add();
    }

    @Test
    public void test2(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean4.xml");
        Emp emp = applicationContext.getBean("emp", Emp.class);
        System.out.println(emp.toString());
    }
}
