package com.spring5.service;

import com.spring5.dao.UserDao;
import com.spring5.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    //注入dao
    @Autowired
    private UserDao userDao;


    public void add(User user){
        userDao.add(user);
    }

    public void update(User user) {
        userDao.update(user);
    }
    public void delete(String id) {
        userDao.delete(id);
    }

    public int queryCount() {
        return userDao.queryCount();
    }

    public User queryOne(String id) {
        return userDao.queryOne(id);
    }

    public List<User> querList() {
        return userDao.querList();
    }

    public void batchAdd(List<Object[]> list){
        userDao.batchAdd(list);
    }
    public void batchUpdate(List<Object[]> list){
        userDao.batchUpdate(list);
    }

    public void batchDelete(List<Object[]> list){
        userDao.batchDelete(list);
    }
}
