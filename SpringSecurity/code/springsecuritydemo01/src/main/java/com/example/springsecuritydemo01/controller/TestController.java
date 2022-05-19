package com.example.springsecuritydemo01.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String hello(){
        return "hello security";
    }

    @GetMapping("/index")
    public String index(){
        return "hello index";
    }

    @GetMapping("/update")
    //拥有其中一个角色才能访问
    //@Secured({"ROLE_sale","ROLE_manager"})
    //进入方法前做验证 可以时角色或者 权限 hasAnyRole hasAnyAuthority
    //@PreAuthorize("hasAnyAuthority('admins')")
    //执行完方法 返回时验证
//    @PostAuthorize("hasAnyAuthority('admin')")
//    @PreFilter()
//    @PostFilter()
    public String update(){
        System.out.println("update...");
        return "hello update!";
    }
}
