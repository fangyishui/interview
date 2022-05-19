package com.spring5.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration  //作为配置类 代替xml配置文件
@ComponentScan(basePackages = {"com.spring5"})  //包扫描
public class SpringConfig {
}
