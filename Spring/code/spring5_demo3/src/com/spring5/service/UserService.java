package com.spring5.service;

import com.spring5.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


//注解里面value属性值可以省略不写，默认值时类名称 首字母小学
//@Component
//@Component("userService")
//@Component(value = "userService")    //相当于 <bean id ="" class = ""/>
//@Controller
@Service
public class UserService {

    //定义dao类型属性
    //不需要添加set方法
    //添加注入属性注解
//    @Autowired  //根据类型注入
//    @Qualifier(value = "userDaoImplTe") //根据名称（value）进行注入


    @Value(value = "hello")
    private String str;

    //@Resource   //默认 没有定义 name值时 根据类型注入
    @Resource(name = "userDaoImplTe")   // name有值时 根据名称注入
    private UserDao userDao;

    public void add(){
        System.out.println("service add ....."+str);
        userDao.add();
    }
}
