package com.webflux.demoreactor8.reactor8;

import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TestReactor {

    public static void main(String[] args) {

//        WebHandler

        //just 方法直接声明
        Flux.just(1,2,3,5).subscribe(System.out::println);
        Mono.just(2).subscribe(System.out::println);

        //其他的方法
        Integer[] array = {1,2,3,4};
        Flux.fromArray(array);

        //集合
        List<Integer> list = Arrays.asList(array);
        Flux.fromIterable(list);

        //流
        Stream<Integer> stream = list.stream();
        Flux.fromStream(stream);
    }
}
