## 概述

Feign：  美[feɪn]英[feɪn]




官网解释：
http://projects.spring.io/spring-cloud/spring-cloud.html#spring-cloud-feign

 Feign是一个声明式WebService客户端。使用Feign能让编写Web Service客户端更加简单, 它的使用方法是定义一个接口，然后在上面添加注解，同时也支持JAX-RS标准的注解。Feign也支持可拔插式的编码器和解码器。Spring Cloud对Feign进行了封装，使其支持了Spring MVC标准注解和HttpMessageConverters。Feign可以与Eureka和Ribbon组合使用以支持负载均衡。
 ；

 Feign是一个声明式的Web服务客户端，使得编写Web服务客户端变得非常容易，
只需要创建一个接口，然后在上面添加注解即可。
参考官网：https://github.com/OpenFeign/feign 

 Feign能干什么
Feign旨在使编写Java Http客户端变得更容易。
前面在使用Ribbon+RestTemplate时，利用RestTemplate对http请求的封装处理，形成了一套模版化的调用方法。但是在实际开发中，由于对服务依赖的调用可能不止一处，往往一个接口会被多处调用，所以通常都会针对每个微服务自行封装一些客户端类来包装这些依赖服务的调用。所以，Feign在此基础上做了进一步封装，由他来帮助我们定义和实现依赖服务接口的定义。在Feign的实现下，我们只需创建一个接口并使用注解的方式来配置它(以前是Dao接口上面标注Mapper注解,现在是一个微服务接口上面标注一个Feign注解即可)，即可完成对服务提供方的接口绑定，简化了使用Spring cloud Ribbon时，自动封装服务调用客户端的开发量。

Feign集成了Ribbon
利用Ribbon维护了MicroServiceCloud-Dept的服务列表信息，并且通过轮询实现了客户端的负载均衡。而与Ribbon不同的是，通过feign只需要定义服务绑定接口且以声明式的方法，优雅而简单的实现了服务调用



## Feign使用步骤

### 参考microservicecloud-consumer-dept-80



### 新建microservicecloud-consumer-dept-feign

修改主启动类名字：DeptConsumer80_Feign_App



### microservicecloud-consumer-dept-feign工程pom.xml修改，主要添加对feign的支持

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-feign</artifactId>
</dependency>
```



### 修改microservicecloud-api工程

POM

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-feign</artifactId>
</dependency>
```



新建DeptClientService接口并新增注解@FeignClient

```java
@FeignClient(value = "MICROSERVICECLOUD-DEPT")
public interface DeptClientService
{
    @RequestMapping(value = "/dept/get/{id}",method = RequestMethod.GET)
    public Dept get(@PathVariable("id") long id);

    @RequestMapping(value = "/dept/list",method = RequestMethod.GET)
    public List<Dept> list();

    @RequestMapping(value = "/dept/add",method = RequestMethod.POST)
    public boolean add(Dept dept);
}
```



mvn clean

mvn install



microservicecloud-consumer-dept-feign工程修改Controller，添加上一步新建的DeptClientService接口

```java
@RestController
public class DeptController_Feign
{
    @Autowired
    private DeptClientService service = null;

    @RequestMapping(value = "/consumer/dept/get/{id}")
    public Dept get(@PathVariable("id") Long id)
    {
        return this.service.get(id);
    }

    @RequestMapping(value = "/consumer/dept/list")
    public List<Dept> list()
    {
        return this.service.list();
    }

    @RequestMapping(value = "/consumer/dept/add")
    public Object add(Dept dept)
    {
        return this.service.add(dept);
    }
}
```



microservicecloud-consumer-dept-feign工程修改主启动类

```java
@SpringBootApplication
@EnableEurekaClient
//@RibbonClient(name="MICROSERVICECLOUD-DEPT",configuration= MySelfRule.class)
@EnableFeignClients(basePackages= {"com"})
@ComponentScan("com")
public class DeptConsumer80_Feign_App {

    public static void main(String[] args) {

        SpringApplication.run(DeptConsumer80_Feign_App.class,args);
    }
}
```

@EnableFeignClients和@FeignClient对应开启和使用



测试:

启动3个eureka集群

启动3个部门微服务8001/8002/8003

启动Feign自己启动

http://localhost/consumer/dept/list

Feign自带负载均衡配置项



小总结：


   Feign通过接口的方法调用Rest服务（之前是Ribbon+RestTemplate），
该请求发送给Eureka服务器（http://MICROSERVICECLOUD-DEPT/dept/list）,
通过Feign直接找到服务接口，由于在进行服务调用的时候融合了Ribbon技术，所以也支持负载均衡作用。

 

 