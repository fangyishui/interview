package com.example.webfluxdemo1;

import com.example.webfluxdemo1.handler.Userhandler;
import com.example.webfluxdemo1.service.UserService;
import com.example.webfluxdemo1.service.impl.UserServiceImpl;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.netty.http.server.HttpServer;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;

public class Server {


    public static void main(String[] args) throws Exception{
        Server server = new Server();
        server.createReactorServer();
        System.out.println("enter to exit");
        System.in.read();
    }

    //1 创建 Router 路由
    public RouterFunction<ServerResponse> routerFunction(){

        UserService userService = new UserServiceImpl();
        //创建 handler 对象
        Userhandler userhandler = new Userhandler(userService);
        //设置路由
        return RouterFunctions.route(
                GET("/user/{id}").and(accept(APPLICATION_JSON)),userhandler::getUserById)
                .andRoute(GET("/users").and(accept(APPLICATION_JSON)),userhandler::getAll);
    }

    //2 创建服务器 完成适配
    public void createReactorServer(){
        //路由和handler适配
        RouterFunction<ServerResponse> router = routerFunction();
        HttpHandler httpHandler = toHttpHandler(router);

        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);

        //创建服务器
        HttpServer httpServer = HttpServer.create();
        httpServer.handle(adapter).bindNow();

    }
}
