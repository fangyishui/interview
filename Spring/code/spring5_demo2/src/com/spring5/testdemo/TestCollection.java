package com.spring5.testdemo;

import com.spring5.autowire.Emp;
import com.spring5.bean.Orders;
import com.spring5.collectiontype.Book;
import com.spring5.collectiontype.Stu;
import com.spring5.factorybean.Mybean;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestCollection {


    @Test
    public void testCollection(){

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean1.xml");
        Stu stu = applicationContext.getBean("stu", Stu.class);

        System.out.println(stu.toString());
    }


    @Test
    public void testCollection2(){

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean2.xml");
//        Book book = applicationContext.getBean("book", Book.class);
        Book book1 = applicationContext.getBean("book", Book.class);
        Book book2 = applicationContext.getBean("book", Book.class);
        System.out.println(book1);
        System.out.println(book2);
    }

    @Test
    public void test3(){

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean3.xml");
        Book myBean = applicationContext.getBean("myBean", Book.class);

        System.out.println(myBean);
    }

    @Test
    public void test4(){
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean4.xml");
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean4.xml");
        Orders orders = applicationContext.getBean("orders", Orders.class);
        System.out.println("第四步 获取创建的bean实例对象");
        System.out.println(orders);

        //手动让bean实例销毁
        applicationContext.close();
    }

    @Test
    public void test5(){

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean5.xml");
        Emp emp = applicationContext.getBean("emp", Emp.class);

        System.out.println(emp);
    }
}
