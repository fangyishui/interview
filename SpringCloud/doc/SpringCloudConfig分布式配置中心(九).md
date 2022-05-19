## 概述

分布式系统面临的---配置问题



微服务意味着要将单体应用中的业务拆分成一个个子服务，每个服务的粒度相对较小，因此系统中会出现大量的服务。由于每个服务都需要必要的配置信息才能运行，所以一套集中式的、动态的配置管理设施是必不可少的。SpringCloud提供了ConfigServer来解决这个问题，我们每一个微服务自己带着一个application.yml，上百个配置文件的管理....../(ㄒoㄒ)/~~



### 是什么

![file](images/SpringCloudConfig分布式配置中心(九)/image-1650459505150.png)

SpringCloud Config为微服务架构中的微服务提供集中化的外部配置支持，配置服务器为各个不同微服务应用的所有环境提供了一个中心化的外部配置。

怎么玩
SpringCloud Config分为服务端和客户端两部分。

服务端也称为分布式配置中心，它是一个独立的微服务应用，用来连接配置服务器并为客户端提供获取配置信息，加密/解密信息等访问接口

客户端则是通过指定的配置中心来管理应用资源，以及与业务相关的配置内容，并在启动的时候从配置中心获取和加载配置信息配置服务器默认采用git来存储配置信息，这样就有助于对环境配置进行版本管理，并且可以通过git客户端工具来方便的管理和访问配置内容。



### 能干嘛

集中管理配置文件

不同环境不同配置，动态化的配置更新，分环境部署比如dev/test/prod/beta/release

运行期间动态调整配置，不再需要在每个服务部署的机器上编写配置文件，服务会向配置中心统一拉取配置自己的信息

当配置发生变动时，服务不需要重启即可感知到配置的变化并应用新的配置

将配置信息以REST接口的形式暴露



与GitHub整合配置

由于SpringCloud Config默认使用Git来存储配置文件(也有其它方式,比如支持SVN和本地文件)，
但最推荐的还是Git，而且使用的是http/https访问的形式



## SpringCloud Config服务端配置



- 用自己的GitHub账号在GitHub上新建一个名为microservicecloud-config
  的新Repository

- 由上一步获得SSH协议的git地址

  git@github.com:fangyishui/microservicecloud-config.git

- 本地硬盘目录上新建git仓库并clone

  本地地址：D:\44\mySpringCloud

  git命令：git clone git@github.com:fangyishui/microservicecloud-config.git

- 在本地D:\44\mySpringCloud\microservicecloud-config里面新建一个application.yml

  保存格式必须为UTF-8

  ```yaml
  spring:
    profiles:
      active:
      - dev
  ---
  spring:
    profiles: dev     #开发环境
    application:
      name: microservicecloud-config-atguigu-dev
  ---
  spring:
    profiles: test   #测试环境
    application:
      name: microservicecloud-config-atguigu-test
  #  请保存为UTF-8格式
  ```

- 将上一步的YML文件推送到github上

  ```shell
  git add .
  git commit -m "init yml"
  git push origin master
  ```

- 新建Module模块microservicecloud-config-3344
  它即为Cloud的配置中心模块

POM

```xml
<dependencies>
        <!-- springCloud Config -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
        <!-- 避免Config的Git插件报错：org/eclipse/jgit/api/TransportConfigCallback  -->
<!--        <dependency>-->
<!--            <groupId>org.eclipse.jgit</groupId>-->
<!--            <artifactId>org.eclipse.jgit</artifactId>-->
<!--            <version>4.10.0.201712302008-r</version>-->
<!--        </dependency>-->
        <!-- 图形化监控 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <!-- 熔断 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-hystrix</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
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
  port: 3344

spring:
  application:
    name:  microservicecloud-config
  cloud:
    config:
      server:
        git:
          uri: git@github.com:fangyishui/microservicecloud-config.git #GitHub上面的git仓库名字
```

主启动类Config_3344_StartSpringCloudApp

  ```java
@SpringBootApplication
@EnableConfigServer
public class Config_3344_StartSpringCloudApp
{
    public static void main(String[] args)
    {
        SpringApplication.run(Config_3344_StartSpringCloudApp.class,args);
    }
}
  ```

- windows下修改hosts文件，增加映射

  127.0.0.1  config-3344.com

- 测试通过Config微服务是否可以从GitHub上获取配置内容

  启动微服务3344

  http://config-3344.com:3344/application-dev.yml

  http://config-3344.com:3344/application-test.yml

  http://config-3344.com:3344/application-xxx.yml(不存在的配置)



**需要关闭代理程序**



### 配置读取规则

官网

![file](images/SpringCloudConfig分布式配置中心(九)/image-1650460349473.png)




/{application}-{profile}.yml

http://config-3344.com:3344/application-dev.yml

http://config-3344.com:3344/application-test.yml

http://config-3344.com:3344/application-xxx.yml(不存在的配置)



/{application}/{profile}[/{label}]

http://config-3344.com:3344/application/dev/master

http://config-3344.com:3344/application/test/master

http://config-3344.com:3344/application/xxx/master



/{label}/{application}-{profile}.yml

http://config-3344.com:3344/master/application-dev.yml

http://config-3344.com:3344/master/application-test.yml



成功实现了用SpringCloud Config通过GitHub获取配置信息





## SpringCloud Config客户端配置与测试

在本地D:\44\mySpringCloud\microservicecloud-config路径下新建文件
microservicecloud-config-client.yml



microservicecloud-config-client.yml内容

```yaml
spring:
  profiles:
    active:
    - dev
---
server: 
  port: 8201 
spring:
  profiles: dev
  application: 
    name: microservicecloud-config-client
eureka: 
  client: 
    service-url: 
      defaultZone: http://eureka-dev.com:7001/eureka/   
---
server: 
  port: 8202 
spring:
  profiles: test
  application: 
    name: microservicecloud-config-client
eureka: 
  client: 
    service-url: 
      defaultZone: http://eureka-test.com:7001/eureka/
```



将上一步提交到GitHub中



新建microservicecloud-config-client-3355



POM

```xml
<dependencies>
   <!-- SpringCloud Config客户端 -->
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-config</artifactId>
   </dependency> 
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-actuator</artifactId>
   </dependency>
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-hystrix</artifactId>
   </dependency>
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-eureka</artifactId>
   </dependency>
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-config</artifactId>
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



bootstrap.yml


applicaiton.yml是用户级的资源配置项
bootstrap.yml是系统级的，优先级更加高

Spring Cloud会创建一个`Bootstrap Context`，作为Spring应用的`Application Context`的父上下文。初始化的时候，`Bootstrap Context`负责从外部源加载配置属性并解析配置。这两个上下文共享一个从外部获取的`Environment`。`Bootstrap`属性有高优先级，默认情况下，它们不会被本地配置覆盖。 `Bootstrap context`和`Application Context`有着不同的约定，
所以新增了一个`bootstrap.yml`文件，保证`Bootstrap Context`和`Application Context`配置的分离。

```yaml
spring:
  cloud:
    config:
      name: microservicecloud-config-client #需要从github上读取的资源名称，注意没有yml后缀名
      profile: dev   #本次访问的配置项
      label: master   
      uri: http://config-3344.com:3344  #本微服务启动后先去找3344号服务，通过SpringCloudConfig获取GitHub的服务地址
 


```



application.yml

```yaml
spring:
  application:
    name: microservicecloud-config-client
```



windows下修改hosts文件，增加映射

```shell
127.0.0.1  client-config.com
```





新建rest类，验证是否能从GitHub上读取配置

```java
@RestController
public class ConfigClientRest {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${eureka.client.service-url.defaultZone}")
    private String eurekaServers;

    @Value("${server.port}")
    private String port;

    @RequestMapping("/config")
    public String getConfig()
    {
        String str = "applicationName: "+applicationName+"\t eurekaServers:"+eurekaServers+"\t port: "+port;
        System.out.println("******str: "+ str);
        return "applicationName: "+applicationName+"\t eurekaServers:"+eurekaServers+"\t port: "+port;
    }
}
```



主启动类ConfigClient_3355_StartSpringCloudApp

```java
@SpringBootApplication
public class ConfigClient_3355_StartSpringCloudApp
{
  public static void main(String[] args)
  {
   SpringApplication.run(ConfigClient_3355_StartSpringCloudApp.class,args);
  }
}
```





测试

启动Config配置中心3344微服务并自测

http://config-3344.com:3344/application-dev.yml



启动3355作为Client准备访问



bootstrap.yml里面的profile值是什么，决定从github上读取什么

假如目前是 profile: dev

	dev默认在github上对应的端口就是8201
	
	http://client-config.com:8201/config

假如目前是 profile: test

	test默认在github上对应的端口就是8202
	
	http://client-config.com:8202/config





成功实现了客户端3355访问SpringCloud Config3344通过GitHub获取配置信息





## SpringCloud Config配置实战

	目前情况


1  Config服务端配置配置OK且测试通过，我们可以和config+GitHub进行配置修改并获得内容

2 此时我们做一个eureka服务+一个Dept访问的微服务，将两个微服务的配置统一由于github获得实现统一配置分布式管理，完成多环境的变更



步骤



### Git配置文件本地配置

在本地D:\44\mySpringCloud\microservicecloud-config路径下新建文件
microservicecloud-config-eureka-client.yml



microservicecloud-config-eureka-client.yml内容

```yaml
spring: 
  profiles: 
    active: 
    - dev
---
server: 
  port: 7001 #注册中心占用7001端口,冒号后面必须要有空格
   
spring: 
  profiles: dev
  application:
    name: microservicecloud-config-eureka-client
    
eureka: 
  instance: 
    hostname: eureka7001.com #冒号后面必须要有空格
  client: 
    register-with-eureka: false #当前的eureka-server自己不注册进服务列表中
    fetch-registry: false #不通过eureka获取注册信息
    service-url: 
      defaultZone: http://eureka7001.com:7001/eureka/
---
server: 
  port: 7001 #注册中心占用7001端口,冒号后面必须要有空格
   
spring: 
  profiles: test
  application:
    name: microservicecloud-config-eureka-client
    
eureka: 
  instance: 
    hostname: eureka7001.com #冒号后面必须要有空格
  client: 
    register-with-eureka: false #当前的eureka-server自己不注册进服务列表中
    fetch-registry: false #不通过eureka获取注册信息
    service-url: 
      defaultZone: http://eureka7001.com:7001/eureka/
```



在本地D:\44\mySpringCloud\microservicecloud-config路径下新建文件
microservicecloud-config-dept-client.yml



microservicecloud-config-dept-client.yml内容

```yaml
spring: 
  profiles:
    active:
    - dev
--- 
server:
  port: 8001
spring: 
   profiles: dev
   application: 
    name: microservicecloud-config-dept-client
   datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: org.gjt.mm.mysql.Driver
    url: jdbc:mysql://localhost:3306/cloudDB01
    username: root
    password: 123456
    dbcp2:
      min-idle: 5
      initial-size: 5
      max-total: 5
      max-wait-millis: 200 
mybatis:
  config-location: classpath:mybatis/mybatis.cfg.xml
  type-aliases-package: com.atguigu.springcloud.entities
  mapper-locations:
  - classpath:mybatis/mapper/**/*.xml
 
eureka: 
  client: #客户端注册进eureka服务列表内
    service-url: 
      defaultZone: http://eureka7001.com:7001/eureka
  instance:
    instance-id: dept-8001.com
    prefer-ip-address: true
 
info:
  app.name: atguigu-microservicecloud-springcloudconfig01
  company.name: www.atguigu.com
  build.artifactId: $project.artifactId$
  build.version: $project.version$
---
server:
  port: 8001
spring: 
   profiles: test
   application: 
    name: microservicecloud-config-dept-client
   datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: org.gjt.mm.mysql.Driver
    url: jdbc:mysql://localhost:3306/cloudDB02
    username: root
    password: 123456
    dbcp2:
      min-idle: 5
      initial-size: 5
      max-total: 5
      max-wait-millis: 200  
  
  
mybatis:
  config-location: classpath:mybatis/mybatis.cfg.xml
  type-aliases-package: com.atguigu.springcloud.entities
  mapper-locations:
  - classpath:mybatis/mapper/**/*.xml
 
eureka: 
  client: #客户端注册进eureka服务列表内
    service-url: 
      defaultZone: http://eureka7001.com:7001/eureka
  instance:
    instance-id: dept-8001.com
    prefer-ip-address: true
 
info:
  app.name: atguigu-microservicecloud-springcloudconfig02
  company.name: www.atguigu.com
  build.artifactId: $project.artifactId$
  build.version: $project.version$
```



### Config版的eureka服务端

新建工程microservicecloud-config-eureka-client-7001



POM

```xml
<dependencies>
    <!-- SpringCloudConfig配置 -->
    <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-config</artifactId>
    </dependency> 
    <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-eureka-server</artifactId>
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



bootstrap.yml

```yaml
spring: 
  cloud: 
    config: 
      name: microservicecloud-config-eureka-client     #需要从github上读取的资源名称，注意没有yml后缀名
      profile: dev 
      label: master 
      uri: http://config-3344.com:3344      #SpringCloudConfig获取的服务地址
```



application.yml

```yaml
spring:
  application:
    name: microservicecloud-config-eureka-client
```



主启动类Config_Git_EurekaServerApplication

```java
/**
 * EurekaServer服务器端启动类,接受其它微服务注册进来
 * @author zhouyang
 */
@SpringBootApplication 
@EnableEurekaServer 
public class Config_Git_EurekaServerApplication 
{
  public static void main(String[] args) 
  {
   SpringApplication.run(Config_Git_EurekaServerApplication.class, args);
  }
}
```



测试

先启动microservicecloud-config-3344微服务，保证Config总配置是OK的

再启动microservicecloud-config-eureka-client-7001微服务

http://eureka7001.com:7001/

出现eureak主页表示成功启动



### Config版的dept微服务

参考之前的8001拷贝后新建工程microservicecloud-config-dept-client-8001



POM

```xml
<dependencies>
        <!-- SpringCloudConfig配置 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>microservicecloud-api</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-core</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
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



bootstrap.yml

```yaml
spring:
  cloud:
    config:
      name: microservicecloud-config-dept-client #需要从github上读取的资源名称，注意没有yml后缀名
      #profile配置是什么就取什么配置dev or test
      #profile: dev
      profile: test
      label: master
      uri: http://config-3344.com:3344  #SpringCloudConfig获取的服务地址
```



application.yml

```yaml
spring:
  application:
    name: microservicecloud-config-dept-client
```



主启动类及其它一套业务逻辑代码

主启动类

```java
@SpringBootApplication
@EnableEurekaClient //本服务启动后会自动注册进eureka服务中
@EnableDiscoveryClient //服务发现
public class DeptProvider8001_App
{
  public static void main(String[] args)
  {
   SpringApplication.run(DeptProvider8001_App.class, args);
  }
}
```



其它业务逻辑代码

service和dao相关代码



配置说明

```yaml
spring:
  cloud:
    config:
      name: microservicecloud-config-dept-client #需要从github上读取的资源名称，注意没有yml后缀名
      #profile配置是什么就取什么配置dev or test
      #profile: dev
      profile: test
      label: master
      uri: http://config-3344.com:3344  #SpringCloudConfig获取的服务地址
```

主要看bootstrap.yml文件里面的
profile: 属性具体值是什么，从而确定它能从github上取得什么样的配置
假如配置dev左图，如果配置test那就找右图，具体各自数据库不同，从而依据配置得到分布式配置的目的

 

 测试



test配置默认访问

	http://localhost:8001/dept/list
	
	可以看到数据库配置是02



本地换配置成dev

	http://localhost:8001/dept/list
	
	可以看到数据库配置是01



## 总结：

3344 模块连接远程git服务器，读取配置，根据不同profile 读取不同配置。

7001管理注册中心，读取远程配置。

8001作为服务提供者，读取远程配置。