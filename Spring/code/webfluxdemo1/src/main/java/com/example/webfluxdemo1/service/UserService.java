package com.example.webfluxdemo1.service;

import com.example.webfluxdemo1.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//用户操作接口
public interface UserService {

    //根据id查询用户
    Mono<User> getUserById(int id);
    //查询所有用户
    Flux<User> getAll();
    //添加用户
    Mono<Void> saveUser(Mono<User> userMono);
}
