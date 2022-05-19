package com.spring5.testdemo;

import com.spring5.Book;
import com.spring5.Orders;
import com.spring5.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpring5 {


    @Test
    public void testAdd(){
        //1.加载spring配置文件
        ApplicationContext context = new ClassPathXmlApplicationContext("bean1.xml");
        //2.获取配置创建的对象
        User user = context.getBean("user", User.class);
        System.out.println(user);
        user.add();
    }

    @Test
    public void testSetMethod(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean1.xml");
        Book book = ac.getBean("book", Book.class);
        System.out.println(book.toString());
    }

    @Test
    public void testConstructor(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean1.xml");
        Orders orders = applicationContext.getBean("orders", Orders.class);
        System.out.println(orders);
    }
}
