package com.spring5.test;

import com.spring5.config.TxConfig;
import com.spring5.service.TransferService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;


public class TestJdbc {

    @Nullable
    private String name;

    @Test
    public void testTx(){

        ApplicationContext context = new ClassPathXmlApplicationContext("bean1.xml");

        TransferService transferService = context.getBean("transferService", TransferService.class);

        transferService.transfer();
    }
    @Test
    public void testTx2(){

        ApplicationContext context = new ClassPathXmlApplicationContext("bean2.xml");

        TransferService transferService = context.getBean("transferService", TransferService.class);

        transferService.transfer();
    }

    @Test
    public void testTx3(){

        ApplicationContext context = new AnnotationConfigApplicationContext(TxConfig.class);

        TransferService transferService = context.getBean("transferService", TransferService.class);

        transferService.transfer();
    }

    //函数式风格创建对象 交给spring管理
    @Test
    public void testGenericApplicationContext(){
        //1.创建GenericApplicationContext对象
        GenericApplicationContext context = new GenericApplicationContext();
        //2.调用context的方法对象注册
        context.refresh();
        context.registerBean("user2",User.class,() ->new User());
        //3.获取在spring注册的对象
        //(1) 通过类 全路径找到对象，beanName可以为空 不为空时：指定获取的名称是什么
        //User user = (User) context.getBean("com.spring5.test.User");
        //(2) 通过beanName
        User user = (User) context.getBean("user2");
        System.out.println(user);

    }
}
