## 总体介绍

### 以Dept部门模块做一个微服务通用案例

Consumer消费者（Client）通过REST调用Provider提供者（Server）提供的服务

### 一个简单的Maven模块结构是这样的：

```java
 
一个简单的Maven模块结构是这样的：
---- app-parent      一个父项目(app-parent)聚合很多子项目(app-util,app-dao,app-service,app-web)
      |---- pom.xml (pom)
      |
      |-------- app-util
      | |-------- pom.xml (jar)
      |
      |-------- app-dao
      | |-------- pom.xml (jar)
      |
      |-------- app-service
      | |-------- pom.xml (jar)
      |
      |-------- app-web
        |-------- pom.xml (war) 
 
 


```

MicroServiceCloud父工程（Project）下初次带着3个子模块（Module）
microservicecloud-api 封装的整体entity/接口/公共配置等
microservicecloud-provider-dept-8001 微服务落地的服务提供者
microservicecloud-consumer-dept-80 微服务调用的客户端使用

### 80端口

80端口是为HTTP(HyperText Transport Protocol)即超文本传输协议开放的
此为上网冲浪使用次数最多的协议，主要用于WWW(World Wide Web)即万维网传输信息的协议。

可以通过HTTP地址(即常说的"网址")加":80"来访问网站，

因为浏览网页服务默认的端口号都是80，因此只需输入网址即可，不用输入":80"了。



### RestTemplate介绍

RestTemplate提供了多种便捷访问远程Http服务的方法， 
是一种简单便捷的访问restful服务模板类，是Spring提供的用于访问Rest服务的客户端模板工具集

官网地址

https://docs.spring.io/spring-framework/docs/4.3.7.RELEASE/javadoc-api/org/springframework/web/client/RestTemplate.html

使用
使用restTemplate访问restful接口非常的简单粗暴无脑。
(url, requestMap, ResponseBean.class)这三个参数分别代表 
REST请求地址、请求参数、HTTP响应转换被转换成的对象类型。




### 本次springCloud版本

```xml
<dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Dalston.SR1</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>1.5.9.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
```


## 一.microservicecloud

整体父工程Project

### 1.新建父工程microservicecloud，切记是Packageing是pom模式

### 2.主要是定义POM文件，将后续各个子模块公用的jar包等统一提出来，类似一个抽象父类

### 3.pom文件：

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
 
  <groupId>com.example</groupId>
    <artifactId>microservicecloud</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>pom</packaging>
 
 
  <properties>
   <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
   <maven.compiler.source>1.8</maven.compiler.source>
   <maven.compiler.target>1.8</maven.compiler.target>
   <junit.version>4.12</junit.version>
   <log4j.version>1.2.17</log4j.version>
   <lombok.version>1.16.18</lombok.version>
  </properties>
 
  <dependencyManagement>
   <dependencies>
     <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-dependencies</artifactId>
       <version>Dalston.SR1</version>
       <type>pom</type>
       <scope>import</scope>
     </dependency>
     <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-dependencies</artifactId>
       <version>1.5.9.RELEASE</version>
       <type>pom</type>
       <scope>import</scope>
     </dependency>
     <!--            <dependency>-->
<!--                <groupId>mysql</groupId>-->
<!--                <artifactId>mysql-connector-java</artifactId>-->
<!--                <version>5.0.4</version>-->
<!--            </dependency>-->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>8.0.23</version>
            </dependency>
     <dependency>
       <groupId>com.alibaba</groupId>
       <artifactId>druid</artifactId>
       <version>1.0.31</version>
     </dependency>
     <dependency>
       <groupId>org.mybatis.spring.boot</groupId>
       <artifactId>mybatis-spring-boot-starter</artifactId>
       <version>1.3.0</version>
     </dependency>
     <dependency>
       <groupId>ch.qos.logback</groupId>
       <artifactId>logback-core</artifactId>
       <version>1.2.3</version>
     </dependency>
     <dependency>
       <groupId>junit</groupId>
       <artifactId>junit</artifactId>
       <version>${junit.version}</version>
       <scope>test</scope>
     </dependency>
     <dependency>
       <groupId>log4j</groupId>
       <artifactId>log4j</artifactId>
       <version>${log4j.version}</version>
     </dependency>
   </dependencies>
  </dependencyManagement>
</project>


```

## 二.microservicecloud-api

公共子模块Module

### 1.新建microservicecloud-api

创建完成后请回到父工程查看pom文件变化

```xml
    <modules>
        <module>microservicecloud-api</module>
    </modules>
```

### 2.修改POM

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent><!-- 子类里面显示声明才能有明确的继承表现，无意外就是父类的默认版本否则自己定义 -->
        <groupId>com.example</groupId>
        <artifactId>microservicecloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>

    <artifactId>microservicecloud-api</artifactId><!-- 当前Module我自己叫什么名字 -->

    <dependencies><!-- 当前Module需要用到的jar包，按自己需求添加，如果父类已经包含了，可以不用写版本号 -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-feign</artifactId>
        </dependency>
    </dependencies>

</project>


```

### 3.新建部门Entity且配合lombok使用

```java
@NoArgsConstructor
@Data
@Accessors(chain=true)
public class Dept implements Serializable //必须序列化
{
  private Long  deptno;   //主键
  private String  dname;   //部门名称
  private String  db_source;//来自那个数据库，因为微服务架构可以一个服务对应一个数据库，同一个信息被存储到不同数据库
  
  public Dept(String dname)
  {
   super();
   this.dname = dname;
  }
}
```

### 4.mvn clean install后给其它模块引用，达到通用目的。

也即需要用到部门实体的话，不用每个工程都定义一份，直接引用本模块即可。

## 三.microservicecloud-provider-dept-8001

部门微服务提供者Module

### 1.新建microservicecloud-provider-dept-8001

### 2.POM

```xml
 
 
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
 
  <parent>
        <artifactId>microservicecloud</artifactId>
        <groupId>com.example</groupId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
 
  <artifactId>microservicecloud-provider-dept-8001</artifactId>
 
  <dependencies>
   <!-- 引入自己定义的api通用包，可以使用Dept部门Entity -->
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
   <!-- 修改后立即生效，热部署 -->
   <dependency>
     <groupId>org.springframework</groupId>
     <artifactId>springloaded</artifactId>
   </dependency>
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-devtools</artifactId>
   </dependency>
  </dependencies>
 
</project>

```

### 3.YML

```yaml
server:
  port: 8001
  
mybatis:
  config-location: classpath:mybatis/mybatis.cfg.xml        # mybatis配置文件所在路径
  type-aliases-package: com.example.microservicecloudapi.entity    # 所有Entity别名类所在包
  mapper-locations:
  - classpath:mybatis/mapper/**/*.xml                       # mapper映射文件
    
spring:
   application:
    name: microservicecloud-dept 
   datasource:
    type: com.alibaba.druid.pool.DruidDataSource            # 当前数据源操作类型
    driver-class-name: com.mysql.cj.jdbc.Driver
    #driver-class-name: org.gjt.mm.mysql.Driver              # mysql驱动包
    url: jdbc:mysql://localhost:3307/cloudDB01?serverTimezone=GMT          # 数据库名称
    username: root
    password: root
    dbcp2:
      min-idle: 5                                           # 数据库连接池的最小维持连接数
      initial-size: 5                                       # 初始化连接数
      max-total: 5                                          # 最大连接数
      max-wait-millis: 200                                  # 等待连接获取的最大超时时间
```

### 4.工程src/main/resources目录下新建mybatis文件夹后新建mybatis.cfg.xml文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
  
<configuration>
 
  <settings>
   <setting name="cacheEnabled" value="true"/><!-- 二级缓存开启 -->
  </settings>
 
</configuration>


```

### 5.MySQL创建部门数据库脚本

```sql
DROP DATABASE IF EXISTS cloudDB01;
CREATE DATABASE cloudDB01 CHARACTER SET UTF8;
USE cloudDB01;
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

### 6.DeptDao部门接口

```java
@Mapper
public interface DeptDao {

    public Boolean addDept(Dept dept);

    public Dept findById(Long id);

    public List<Dept> findAll();
}
```

### 7.工程src/main/resources/mybatis目录下新建mapper文件夹后再建DeptMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.springcloud.dao.DeptDao">

	<select id="findById" resultType="Dept" parameterType="Long">
		select deptno,dname,db_source from dept where deptno=#{deptno};
	</select>
	<select id="findAll" resultType="Dept">
		select deptno,dname,db_source from dept;
	</select>
	<insert id="addDept" parameterType="Dept">
		INSERT INTO dept(dname,db_source) VALUES(#{dname},DATABASE());
	</insert>

</mapper>
```

### 8.DeptService部门服务接口

```java
public interface DeptService {
    public Boolean add(Dept dept);

    public Dept get(Long id);

    public List<Dept> list();
}
```

### 9.DeptServiceImpl部门服务接口实现类

```java
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptDao deptDao;

    @Override
    public Boolean add(Dept dept) {
        return deptDao.addDept(dept);
    }

    @Override
    public Dept get(Long id) {
        return deptDao.findById(id);
    }

    @Override
    public List<Dept> list() {
        return deptDao.findAll();
    }
}
```

### 10.DeptController部门微服务提供者REST

```java
@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;

    @PostMapping("/dept/add")
    public Boolean add(@RequestBody Dept dept){
        return deptService.add(dept);
    }

    @GetMapping("/dept/get/{id}")
    public Dept get(@PathVariable("id") Long id){
        return deptService.get(id);
    }


    @GetMapping("/dept/list")
    public List<Dept> list(){
        return deptService.list();
    }
}
```

### 11.DeptProvider8001_App主启动类

```java
@SpringBootApplication
public class DeptProvider_8001 {

    public static void main(String[] args) {
        SpringApplication.run(DeptProvider_8001.class,args);
    }
}

```

### 12.测试

http://localhost:8001/dept/get/2
http://localhost:8001/dept/list



## 四.microservicecloud-consumer-dept-80

部门微服务消费者Module



### 1.新建microservicecloud-consumer-dept-80



### 2.POM

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>microservicecloud</artifactId>
        <groupId>com.example</groupId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>microservicecloud-consumer-dept-80</artifactId>
    <description>部门微服务消费者</description>

    <dependencies>
        <dependency><!-- 自己定义的api -->
            <groupId>com.example</groupId>
            <artifactId>microservicecloud-api</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- 修改后立即生效，热部署 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>springloaded</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
        </dependency>
    </dependencies>

</project>
```



### 3.YML

```yaml
server:
  port: 80
```



### 4.ConfigBean的编写

com.springclod.cfgbean包下ConfigBean的编写（类似spring里面的applicationContext.xml写入的注入Bean）

```java
@Configuration
public class ConfigBean {

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```



### 5.DeptController_Consumer部门微服务消费者

com.springclod.controller包下新建DeptController_Consumer部门微服务消费者REST

```java
@RestController
public class DeptController_Consumer {

    public static final String REST_URL_PREFIX = "http://localhost:8001";

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("/consumer/dept/add")
    public Boolean add(Dept dept){
        return restTemplate.postForObject(REST_URL_PREFIX + "/dept/add",dept, Boolean.class);
    }

    @GetMapping("/consumer/dept/get/{id}")
    public Dept get(@PathVariable Long id){
        return restTemplate.getForObject(REST_URL_PREFIX + "/dept/get/"+id,Dept.class);
    }

    @GetMapping("/consumer/dept/list")
    public List<Dept> list(){
        return restTemplate.getForObject(REST_URL_PREFIX + "/dept/list",List.class);
    }
}
```



###  6.DeptConsumer80_App主启动类

```java
@SpringBootApplication
public class ConsumerDeptApplication_80 {

    public static void main(String[] args) {

        SpringApplication.run(ConsumerDeptApplication_80.class,args);
    }
}
```



### 7.测试

http://localhost/consumer/dept/get/2

http://localhost/consumer/dept/list

http://localhost/consumer/dept/add?dname=AI