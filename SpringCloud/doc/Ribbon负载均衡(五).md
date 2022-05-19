## 概述

 

### 介绍：

ribbon： 美[ˈrɪbən]英['rɪbən]



Spring Cloud Ribbon是基于Netflix Ribbon实现的一套**客户端**       **负载均衡**的工具。

简单的说，Ribbon是Netflix发布的开源项目，主要功能是提供客户端的软件负载均衡算法，将Netflix的中间层服务连接在一起。Ribbon客户端组件提供一系列完善的配置项如连接超时，重试等。简单的说，就是在配置文件中列出Load Balancer（简称LB）后面所有的机器，Ribbon会自动的帮助你基于某种规则（如简单轮询，随机连接等）去连接这些机器。我们也很容易使用Ribbon实现自定义的负载均衡算法。

 

###  功能：

LB（负载均衡）

LB，即负载均衡(Load Balance)，在微服务或分布式集群中经常用的一种应用。
负载均衡简单的说就是将用户的请求平摊的分配到多个服务上，从而达到系统的HA。
常见的负载均衡有软件Nginx，LVS，硬件 F5等。
相应的在中间件，例如：dubbo和SpringCloud中均给我们提供了负载均衡，SpringCloud的负载均衡算法可以自定义。 



###  官网资料：

https://github.com/Netflix/ribbon/wiki/Getting-Started

 

## Ribbon配置初步

1.修改microservicecloud-consumer-dept-80工程

2.修改pom.xml文件

```xml
 <!-- Ribbon相关 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-ribbon</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
```



3.修改application.yml   追加eureka的服务注册地址

```yaml
server:
  port: 80

eureka:
  client:
    register-with-eureka: false
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/,http://eureka7003.com:7003/eureka/
```



4.对ConfigBean进行新注解@LoadBalanced    获得Rest时加入Ribbon的配置

```java
@Configuration
public class ConfigBean {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```



5.主启动类DeptConsumer80_App添加@EnableEurekaClient

```java
@SpringBootApplication
@EnableEurekaClient
public class ConsumerDeptApplication_80 {

    public static void main(String[] args) {

        SpringApplication.run(ConsumerDeptApplication_80.class,args);
    }
}
```



6.修改DeptController_Consumer客户端访问类

```java
private static final String REST_URL_PREFIX = "http://MICROSERVICECLOUD-DEPT";
```



7.先启动3个eureka集群后，再启动microservicecloud-provider-dept-8001并注册进eureka



8.启动microservicecloud-consumer-dept-80



9.测试

http://localhost/consumer/dept/get/1

http://localhost/consumer/dept/list

http://localhost/consumer/dept/add?dname=大数据部

10.小总结

 Ribbon和Eureka整合后Consumer可以直接调用服务而不用再关心地址和端口号

 

## Ribbon负载均衡

1.架构说明
![file](images/Ribbon负载均衡(五)/image-1650422320981.png)

Ribbon在工作时分成两步
第一步先选择 EurekaServer ,它优先选择在同一个区域内负载较少的server.
第二步再根据用户指定的策略，在从server取到的服务注册列表中选择一个地址。
其中Ribbon提供了多种策略：比如轮询、随机和根据响应时间加权。



2.参考microservicecloud-provider-dept-8001，新建两份，分别命名为8002，8003



3.新建8002/8003数据库，各自微服务分别连各自的数据库



```sql
 
DROP DATABASE IF EXISTS cloudDB02;
 
CREATE DATABASE cloudDB02 CHARACTER SET UTF8;
 
USE cloudDB02;
 
CREATE TABLE dept
(
  deptno BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  dname VARCHAR(60),
  db_source   VARCHAR(60)
);
 
INSERT INTO dept(dname,db_source) VALUES('开发部',DATABASE());
INSERT INTO dept(dname,db_source) VALUES('人事部',DATABASE());
INSERT INTO dept(dname,db_source) VALUES('财务部',DATABASE());
INSERT INTO dept(dname,db_source) VALUES('市场部',DATABASE());
INSERT INTO dept(dname,db_source) VALUES('运维部',DATABASE());
 
SELECT * FROM dept;
 
```





```sql
DROP DATABASE IF EXISTS cloudDB03;
 
CREATE DATABASE cloudDB03 CHARACTER SET UTF8;
 
USE cloudDB03;
 
 
CREATE TABLE dept
(
  deptno BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  dname VARCHAR(60),
  db_source   VARCHAR(60)
);
 
INSERT INTO dept(dname,db_source) VALUES('开发部',DATABASE());
INSERT INTO dept(dname,db_source) VALUES('人事部',DATABASE());
INSERT INTO dept(dname,db_source) VALUES('财务部',DATABASE());
INSERT INTO dept(dname,db_source) VALUES('市场部',DATABASE());
INSERT INTO dept(dname,db_source) VALUES('运维部',DATABASE());
 
SELECT * FROM dept;
```



4.修改8002/8003各自YML

端口

数据库链接

对外暴露的统一的服务实例名

```yaml
spring:
   application:
    name: microservicecloud-dept 
```



5.启动3个eureka集群配置区



6.启动3个Dept微服务并各自测试通过

http://localhost:8001/dept/list

http://localhost:8002/dept/list

http://localhost:8003/dept/list



7.启动microservicecloud-consumer-dept-80



8.客户端通过Ribbo完成负载均衡并访问上一步的Dept微服务

http://localhost/consumer/dept/list

注意观察看到返回的数据库名字，各不相同，负载均衡实现

9.总结：

Ribbon其实就是一个软负载均衡的客户端组件，
他可以和其他所需请求的客户端结合使用，和eureka结合只是其中的一个实例。



## Ribbon核心组件IRule

IRule：根据特定算法中从服务列表中选取一个要访问的服务



RoundRobinRule:	轮询

RandomRule:	随机

AvailabilityFilteringRule:	

会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，
还有并发的连接数量超过阈值的服务，然后对剩余的服务列表按照轮询策略进行访问

WeightedResponseTimeRule:

根据平均响应时间计算所有服务的权重，响应时间越快服务权重越大被选中的概率越高。
刚启动时如果统计信息不足，则使用RoundRobinRule策略，等统计信息足够，
会切换到WeightedResponseTimeRule



RetryRule:

先按照RoundRobinRule的策略获取服务，如果获取服务失败则在指定时间内会进行重试，获取可用的服务



BestAvailableRule:

会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量最小的服务



ZoneAvoidanceRule:

默认规则,复合判断server所在区域的性能和server的可用性选择服务器



```java
@Bean
public IRule myRule(){
    return new RandomRule();    //随机
}
```



## Ribbon自定义

1)修改microservicecloud-consumer-dept-80



2)主启动类添加@RibbonClient

在启动该微服务的时候就能去加载我们的自定义Ribbon配置类，从而使配置生效，形如：

```java
@RibbonClient(name="MICROSERVICECLOUD-DEPT",configuration= MySelfRule.class)
```



3)注意配置细节


官方文档明确给出了警告：
这个自定义配置类不能放在@ComponentScan所扫描的当前包下以及子包下，
否则我们自定义的这个配置类就会被所有的Ribbon客户端所共享，也就是说
我们达不到特殊化定制的目的了。

 

 4)步骤

新建package com.rule

```java
package com.rule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySelfRule
{
    @Bean
    public IRule myRule()
    {
        return new RandomRule();//Ribbon默认是轮询，我自定义为随机
    }
}
```

修改主启动类

```java
@SpringBootApplication
@EnableEurekaClient
@RibbonClient(name="MICROSERVICECLOUD-DEPT",configuration= MySelfRule.class)
public class ConsumerDeptApplication_80 {

    public static void main(String[] args) {

        SpringApplication.run(ConsumerDeptApplication_80.class,args);
    }
}
```



测试

http://localhost/consumer/dept/list



5)自定义规则深度解析

问题：依旧轮询策略，但是加上新需求，每个服务器要求被调用5次。也即
以前是每台机器一次，现在是每台机器5次



解析源码：https://github.com/Netflix/ribbon/blob/master/ribbon-loadbalancer/src/main/java/com/netflix/loadbalancer/RandomRule.java

![file](images/Ribbon负载均衡(五)/image-1650422478521.png)



参考源码修改为我们需求要求的RandomRule_Diy.java

```java
public class RandomRule_Diy extends AbstractLoadBalancerRule {

    private int total = 0;    //总共被调用的次数，目前要求每台被调用5次
    private int currentIndex = 0;//当前提供服务的机器号

    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        Server server = null;

        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = lb.getReachableServers();
            List<Server> allList = lb.getAllServers();

            int serverCount = allList.size();
            if (serverCount == 0) {
                /*
                 * No servers. End regardless of pass, because subsequent passes
                 * only get more restrictive.
                 */
                return null;
            }


//            int index = rand.nextInt(serverCount);
//            server = upList.get(index);
            if(total < 5)
            {
                server = upList.get(currentIndex);
                total++;
            }else {
                total = 0;
                currentIndex++;
                if(currentIndex >= upList.size())
                {
                    currentIndex = 0;
                }

            }




            if (server == null) {
                /*
                 * The only time this should happen is if the server list were
                 * somehow trimmed. This is a transient condition. Retry after
                 * yielding.
                 */
                Thread.yield();
                continue;
            }

            if (server.isAlive()) {
                return (server);
            }

            // Shouldn't actually happen.. but must be transient or a bug.
            server = null;
            Thread.yield();
        }

        return server;

    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }
}
```



调用

MySelfRule.java

```java
    @Bean
    public IRule myRule()
    {
        //return new RandomRule();//Ribbon默认是轮询，我自定义为随机
        return new RandomRule_Diy();
    }
```

主启动类

```java
@RibbonClient(name="MICROSERVICECLOUD-DEPT",configuration= MySelfRule.class)
```



测试

http://localhost/consumer/dept/list