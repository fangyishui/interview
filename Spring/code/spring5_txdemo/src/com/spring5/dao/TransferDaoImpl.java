package com.spring5.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TransferDaoImpl implements TransferDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void reduceMoney() {
        String sql = "update account set money = money - ? where name = ?";
        jdbcTemplate.update(sql,100,"Jack");
    }

    @Override
    public void addMoney() {
        String sql = "update account set money = money + ? where name = ?";
        jdbcTemplate.update(sql,100,"Tom");
    }

}
