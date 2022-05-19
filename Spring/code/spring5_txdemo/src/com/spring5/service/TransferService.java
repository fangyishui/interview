package com.spring5.service;

import com.spring5.dao.TransferDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
//@Transactional(readOnly = true,propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
public class TransferService {

    @Autowired
    private TransferDao transferDao;

//    //转帐
    public void transfer() {

        // 减少钱
        transferDao.reduceMoney();

        //模拟异常
        int i = 1/0;
        //增加钱
        transferDao.addMoney();
    }

    //转帐
//    public void transfer() {
//        try{
//            //第一步 开启事务
//            //第二步 进行业务操作
//            // 减少钱
//            transferDao.reduceMoney();
//            //模拟异常
//            int i = 1/0;
//            //增加钱
//            transferDao.addMoney();
//            //第三步 没有发生异常，提交事务
//        }catch (Exception e){
//            //第四步 出现异常，事务回滚
//        }
//    }
}
