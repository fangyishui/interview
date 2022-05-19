## 概述

### 是什么

Zuul包含了对请求的路由和过滤两个最主要的功能：
其中路由功能负责将外部请求转发到具体的微服务实例上，是实现外部访问统一入口的基础而过滤器功能则负责对请求的处理过程进行干预，是实现请求校验、服务聚合等功能的基础.Zuul和Eureka进行整合，将Zuul自身注册为Eureka服务治理下的应用，同时从Eureka中获得其他微服务的消息，也即以后的访问微服务都是通过Zuul跳转后获得。

    注意：Zuul服务最终还是会注册进Eureka

提供=代理+路由+过滤三大功能

 

### 能干嘛

**路由** 

**过滤**

### 官网资料

https://github.com/Netflix/zuul/wiki/Getting-Started





## 路由基本配置



新建Module模块microservicecloud-zuul-gateway-9527



POM

```xml
  <dependencies>
   <!-- zuul路由网关 -->
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-zuul</artifactId>
   </dependency> 
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-eureka</artifactId>
   </dependency>
   <!-- actuator监控 -->
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-actuator</artifactId>
   </dependency>
   <!--  hystrix容错-->
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-hystrix</artifactId>
   </dependency>
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-config</artifactId>
   </dependency>
   <!-- 日常标配 -->
   <dependency>
     <groupId>com.example</groupId>
     <artifactId>microservicecloud-api</artifactId>
     <version>${project.version}</version>
   </dependency>
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-jetty</artifactId>
   </dependency>
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-web</artifactId>
   </dependency>
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-test</artifactId>
   </dependency>
   <!-- 热部署插件 -->
   <dependency>
     <groupId>org.springframework</groupId>
     <artifactId>springloaded</artifactId>
   </dependency>
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-devtools</artifactId>
   </dependency>
  </dependencies>
```



YML

```yaml
server:
  port: 9527

spring:
  application:
    name: microservicecloud-zuul-gateway

eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka,http://eureka7003.com:7003/eureka
  instance:
    instance-id: gateway-9527.com
    prefer-ip-address: true


info:
  app.name: atguigu-microcloud
  company.name: www.atguigu.com
  build.artifactId: $project.artifactId$
  build.version: $project.version$
```



hosts修改

127.0.0.1  myzuul.com



主启动类

@EnableZuulProxy

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
 
@SpringBootApplication
@EnableZuulProxy
public class Zuul_9527_StartSpringCloudApp
{
  public static void main(String[] args)
  {
   SpringApplication.run(Zuul_9527_StartSpringCloudApp.class, args);
  }
}
```



启动

三个eureka集群

一个服务提供类microservicecloud-provider-dept-8001

一个路由



测试

不用路由:http://localhost:8001/dept/get/2

启用路由:http://myzuul.com:9527/microservicecloud-dept/dept/get/2



## 路由访问映射规则

工程microservicecloud-zuul-gateway-9527



代理名称

YML

before
http://myzuul.com:9527/microservicecloud-dept/dept/get/2

```yaml
zuul: 
  routes: 
    mydept.serviceId: microservicecloud-dept
    mydept.path: /mydept/**
```

after
http://myzuul.com:9527/mydept/dept/get/1

将微服务名microservicecloud-dept 代理为mydept

 

此时问题：

http://myzuul.com:9527/mydept/dept/get/1

http://myzuul.com:9527/microservicecloud-dept/dept/get/2

都可以访问



原真实服务名忽略

```yaml
 
zuul: 
  ignored-services: microservicecloud-dept
  #ignored-services: *	#单个具体，多个可以用"*"
  routes: 
    mydept.serviceId: microservicecloud-dept
    mydept.path: /mydept/**
 


```





设置统一公共前缀

```yaml
 
zuul: 
  prefix: /at
  ignored-services: "*"
  routes: 
    mydept.serviceId: microservicecloud-dept
    mydept.path: /mydept/**
```



http://myzuul.com:9527/at/mydept/dept/get/1



最后YML

```yaml
server:
  port: 9527

spring:
  application:
    name: microservicecloud-zuul-gateway

eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka,http://eureka7003.com:7003/eureka
  instance:
    instance-id: gateway-9527.com
    prefer-ip-address: true


info:
  app.name: atguigu-microcloud
  company.name: www.atguigu.com
  build.artifactId: $project.artifactId$
  build.version: $project.version$

zuul:
  prefix: /at
  #ignored-services: microservicecloud-dept
  #单个具体，多个可以用"*"
  ignored-services: "*"
  routes:
    mydept.serviceId: microservicecloud-dept
    mydept.path: /mydept/**
```