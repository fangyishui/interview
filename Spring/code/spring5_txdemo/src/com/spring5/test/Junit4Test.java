package com.spring5.test;

import com.spring5.service.TransferService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) //单元测试框架 SpringJUnit4ClassRunner @ContextConfiguration在spring-test包
@ContextConfiguration("classpath:bean1.xml")//加载配置文件
public class Junit4Test {

    @Autowired
    private TransferService transferService;

    @Test
    public void test1(){
        transferService.transfer();
    }
}
