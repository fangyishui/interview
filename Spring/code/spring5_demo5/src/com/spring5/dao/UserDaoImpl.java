package com.spring5.dao;

import com.spring5.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao{

    //注入JdbcTemplate
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //添加的方法

    /**
     * update(String sql,Object...args)
     * 有两个参数
     * 第一个参数：sq1语句
     * 第二个参数：可变参数，设置sql语句值
     */
    @Override
    public void add(User user) {
        //创建sql语句
        String sql = "insert into user values (? , ? , ?)";
        Object[] args = {user.getUserId(),user.getUsername(),user.getUstatus()};
        int update = jdbcTemplate.update(sql, args);
        System.out.println("影响的行数："+update);
    }

    @Override
    public void update(User user) {
        //创建sql语句
        String sql = "update user set username = ? , ustatus = ? where userId = ?";
        Object[] args = {user.getUsername(),user.getUstatus(),user.getUserId(),};
        int update = jdbcTemplate.update(sql, args);
        System.out.println("影响的行数："+update);
    }

    @Override
    public void delete(String id) {
        String sql = "delete from user where userId = ?";
        int update = jdbcTemplate.update(sql, id);
        System.out.println("影响的行数："+update);
    }

    /**
     * 2、使用JdbcTemplate实现查询返回某个值代码
     * queryForObject(String sql,Class<T>requiredType)
     * 有两个参数
     * 第一个参数：sq1语句
     * 第二个参数：返回类型Class
     */
    @Override
    public int queryCount() {
        String sql = "select count(*) from user";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count;
    }

    /**
     *queryForObject(String sgl,RowMapper<T>rowMapper,Object...args)
     * 有三个参数
     * 第一个参数：sq1语句
     * 第二个参数：RowMapper是接口，针对返回不同类型数据，使用这个接口里面实现类完成数据封装
     * 第三个参数：sq1语句值-参数值
     */
    @Override
    public User queryOne(String id) {
        String sql = "select * from user where userId = ?";
        User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), id);
        return user;
    }

    /**
     * query(String sql,RowMapper<T>rowMapper,Object...args)
     * 有三个参数:
     * 第一个参数：sq1语句
     * 第二个参数：RowMapper是接口，针对返回不同类型数据，使用这个接口里面实现类完成数据封装
     * 第三个参数：sq1语句值-参数值
     */
    @Override
    public List<User> querList() {
        String sql = "select * from user";
        List<User> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class));
        return list;
    }

    @Override
    public void batchAdd(List<Object[]> list) {
        String sql = "insert into user values (? , ? , ?)";
        int[] ints = jdbcTemplate.batchUpdate(sql, list);
        System.out.println("影响的行数："+ Arrays.toString(ints));
    }

    @Override
    public void batchUpdate(List<Object[]> list) {
        String sql = "update user set username = ? , ustatus = ? where userId = ?";
        int[] ints = jdbcTemplate.batchUpdate(sql, list);
        System.out.println("影响的行数："+ Arrays.toString(ints));
    }

    @Override
    public void batchDelete(List<Object[]> list) {
        String sql = "delete from user where userId = ?";
        int[] ints = jdbcTemplate.batchUpdate(sql, list);
        System.out.println("影响的行数："+ Arrays.toString(ints));
    }
}
