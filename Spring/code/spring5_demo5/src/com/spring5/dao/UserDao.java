package com.spring5.dao;

import com.spring5.entity.User;

import java.util.List;

public interface UserDao {

    void add(User user);

    void update(User user);

    void delete(String id);

    int queryCount();

    User queryOne(String id);

    List<User> querList();

    void batchAdd(List<Object[]> list);

    void batchUpdate(List<Object[]> list);

    void batchDelete(List<Object[]> list);
}
