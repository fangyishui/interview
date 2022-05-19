package com.spring5.dao;


import org.springframework.stereotype.Repository;

@Repository("userDaoImplTe")
public class UserDaoImpl implements UserDao{


    @Override
    public void add() {
        System.out.println("dao add .....");
    }
}
