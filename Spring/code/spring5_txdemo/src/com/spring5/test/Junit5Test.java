package com.spring5.test;


import com.spring5.service.TransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration("classpath:bean1.xml")
@SpringJUnitConfig(locations = "classpath:bean1.xml")
public class Junit5Test {

    @Autowired
    private TransferService transferService;

    @Test
    public void test1(){
        transferService.transfer();
    }
}
