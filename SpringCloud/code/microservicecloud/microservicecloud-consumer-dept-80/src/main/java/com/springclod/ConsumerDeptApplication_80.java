package com.springclod;

import com.rule.MySelfRule;
import com.rule.RandomRule_Diy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableEurekaClient
@RibbonClient(name="MICROSERVICECLOUD-DEPT",configuration= MySelfRule.class)
public class ConsumerDeptApplication_80 {

    public static void main(String[] args) {

        SpringApplication.run(ConsumerDeptApplication_80.class,args);
    }
}
