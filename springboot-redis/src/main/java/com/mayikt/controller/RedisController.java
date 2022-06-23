package com.mayikt.controller;

import com.mayikt.utils.RedisLockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    @Autowired
    private RedisLockUtil redisLockUtil;


    @GetMapping("/test1")
    public String test1(String key) throws InterruptedException {
        boolean lock = redisLockUtil.tryLock(key, -1, 3, 1000L);
        if (!lock) {
            return "当前人数过多，请稍后再试";
        }
        System.out.println("========================");
        Thread.sleep(60000);
        redisLockUtil.unlock(key);
        return "redisson";
    }

    //如果不是上锁的线程去解锁，则解锁失败
    @GetMapping("/test2")
    public String test2(String key) throws InterruptedException {
        redisLockUtil.unlock(key);
        return key;
    }
}
