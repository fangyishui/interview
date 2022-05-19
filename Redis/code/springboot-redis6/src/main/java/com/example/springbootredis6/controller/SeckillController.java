package com.example.springbootredis6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Controller
public class SeckillController {

    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/seckill")
    public String seckill(@RequestParam(value = "prodid") String prodid) {

        String userid = new Random().nextInt(50000) + "";
//        Boolean isSuccess = doSecKill(userid,prodid);
        Boolean isSuccess = doSecKillLua(userid,prodid);

        return isSuccess ? "成功" : "失败";
    }

    private Boolean doSecKillLua(String userid,String prodid){
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/redis_stock.lua")));
        Object result = redisTemplate.execute(redisScript, Arrays.asList(userid,prodid));
        String reString = String.valueOf(result);
        System.out.println("lock == " + reString);
        if("0".equals(reString)){
            System.out.println("以抢空！！！");
        }else if("1".equals(reString)){
            System.out.println("抢购成功！！！");
        }else if("2".equals(reString)){
            System.out.println("该用户已抢过！！！");
        }else{
            System.out.println("抢购异常！！！");
        }
        return true;
    }


    private Boolean doSecKill(String uid,String prodid){

        //1 非空判断
        if(uid == null || prodid == null){
            return false;
        }
        //2 拼接key
        String kckey = "sk:"+prodid+":qt";
        String userkey = "sk:"+prodid+":user";

        //增加乐观锁 监控库存
        redisTemplate.watch(kckey);

        //3 获取库存 如果为空 秒杀还没开始
        Integer kc = (Integer) redisTemplate.opsForValue().get(kckey);
        if (kc == null){
            System.out.println("秒杀还没有开始，请等待");
            return false;
        }

        //4 判断用户是否重复秒杀
        if(redisTemplate.opsForSet().isMember(userkey,uid)){
            System.out.println("已经秒杀成功了 不能重复秒杀");
            return false;
        }

        //5 判断如果商品数量 库存量小于1 秒杀结束
        if(kc <= 0){
            System.out.println("秒杀已经结束了 ");
            return false;
        }

        //6 秒杀结束
        //使用事务
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.multi();
        //组队操作
        redisTemplate.opsForValue().decrement(kckey);
        redisTemplate.opsForSet().add(userkey,uid);
        //执行
        List<Object> result = redisTemplate.exec();
        if(result == null || result.size() == 0){
            System.out.println("秒杀失败了。。。");
            return false;
        }

//        // 6.1 库存 -1
//        redisTemplate.opsForValue().decrement(kckey);
//        // 6.2 把秒杀成功用户添加到清单
//        redisTemplate.opsForSet().add(userkey,uid);
        System.out.println("秒杀成功。。。。");

        return true;
    }
}
