package com.spring5.factorybean;

import com.spring5.collectiontype.Book;
import org.springframework.beans.factory.FactoryBean;

import java.util.ArrayList;
import java.util.List;

public class Mybean implements FactoryBean<Book> {

    //返回的对象-可以通过反射修改返回对象
    @Override
    public Book getObject() throws Exception {
        //伪代码
        Book book = new Book();
        List<String> lists = new ArrayList<>();
        lists.add("百年孤独");
        book.setList(lists);
        return book;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
