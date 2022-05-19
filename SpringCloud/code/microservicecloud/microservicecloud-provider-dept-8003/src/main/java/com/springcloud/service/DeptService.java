package com.springcloud.service;

import com.example.microservicecloudapi.entity.Dept;

import java.util.List;

public interface DeptService {
    public Boolean add(Dept dept);

    public Dept get(Long id);

    public List<Dept> list();
}
