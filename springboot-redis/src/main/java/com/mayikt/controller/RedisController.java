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
        boolean lock = redisLockUtil.lock(key, "1", 90000L, 3, 1000L);
        if (!lock) {
            return "当前人数过多，请稍后再试";
        }
        System.out.println("========================");
        Thread.sleep(60000);
        redisLockUtil.unlock(key, "1");
        return "redisson";
    }
}
