package com.spring5.test;

import com.spring5.entity.User;
import com.spring5.service.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class TestJdbc {


    @Test
    public void testCurd(){

        ApplicationContext context = new ClassPathXmlApplicationContext("bean1.xml");

        UserService userService = context.getBean("userService", UserService.class);

        //新增
//        User user = new User();
//        user.setUserId("3");
//        user.setUsername("Tom");
//        user.setUstatus("0");
//        userService.add(user);

        //修改
//        User user = new User();
//        user.setUserId("3");
//        user.setUsername("Tom2");
//        user.setUstatus("1");
//        userService.update(user);

        //删除
//        userService.delete("3");

        //查询-返回某一个值
        int count = userService.queryCount();
        System.out.println("count : "+count);

        //查询-对象
        User user = userService.queryOne("1");
        System.out.println(user);

        //查询-列表
        List<User> users = userService.querList();
        System.out.println(users);
    }

    @Test
    public void testBatchCurd(){

        ApplicationContext context = new ClassPathXmlApplicationContext("bean1.xml");

        UserService userService = context.getBean("userService", UserService.class);

        //批量新增
//        List<Object[]> list = new ArrayList<>();
//        Object[] o1 = {"11","T1","6"};
//        Object[] o2 = {"12","T2","7"};
//        Object[] o3 = {"13","T3","8"};
//        list.add(o1);
//        list.add(o2);
//        list.add(o3);
//        userService.batchAdd(list);

        //批量修改
//        List<Object[]> list = new ArrayList<>();
//        Object[] o1 = {"G1","16","11"};
//        Object[] o2 = {"G1","17","12"};
//        Object[] o3 = {"G1","18","13"};
//        list.add(o1);
//        list.add(o2);
//        list.add(o3);
//        userService.batchUpdate(list);
//
//        //删除
        List<Object[]> list = new ArrayList<>();
        Object[] o1 = {"11"};
        Object[] o2 = {"12"};
        list.add(o1);
        list.add(o2);
        userService.batchDelete(list);

    }
}
