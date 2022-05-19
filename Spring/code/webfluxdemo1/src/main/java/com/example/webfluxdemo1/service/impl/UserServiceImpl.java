package com.example.webfluxdemo1.service.impl;

import com.example.webfluxdemo1.entity.User;
import com.example.webfluxdemo1.service.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    //创建map集合 测试数据
    private final Map<Integer,User> users = new HashMap<>();

    public UserServiceImpl(){
        this.users.put(1,new User("Tom","男",11));
        this.users.put(2,new User("Jack","男",40));
        this.users.put(3,new User("Lucy","女",18));
    }

    //根据id查询
    @Override
    public Mono<User> getUserById(int id) {
        return Mono.justOrEmpty(this.users.get(id));
    }

    //查询所有用户
    @Override
    public Flux<User> getAll() {
        return Flux.fromIterable(this.users.values());
    }
    //添加用户
    @Override
    public Mono<Void> saveUser(Mono<User> userMono) {
        return userMono.doOnNext(person -> {
            //向map集合防放数据
            int id = users.size() + 1;
            users.put(id,person);
        }).thenEmpty(Mono.empty());
    }
}
