package com.example.webfluxdemo1.handler;

import com.example.webfluxdemo1.entity.User;
import com.example.webfluxdemo1.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

public class Userhandler {

    private final UserService userService;

    public Userhandler(UserService userService){
        this.userService = userService;
    }

    //根据id查询
    public Mono<ServerResponse> getUserById(ServerRequest request){
        //获取id值
        Integer id = Integer.valueOf(request.pathVariable("id"));
        //空值处理
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        //调用sever方法得到数据
        Mono<User> userMono = this.userService.getUserById(id);
        //把 userMono 进行转换返回--其中一种写法，可以用下面查询所有写法
        //使用 Reactor操作符 flatMap
       return userMono.flatMap(person ->
            ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(fromObject(person))
                    ).switchIfEmpty(notFound);
    }

    //查询所有
    public Mono<ServerResponse> getAll(ServerRequest request){
        //调用sevice得到结果
        Flux<User> users = this.userService.getAll();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(users,User.class);
    }

    //添加
    public Mono<ServerResponse> saveUser(ServerRequest request){
        //得到user对象
        Mono<User> userMono = request.bodyToMono(User.class);
        return ServerResponse.ok().build(this.userService.saveUser(userMono));
    }
}
