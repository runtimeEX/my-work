package com.mayikt.service.impl;

import com.mayikt.service.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisLockImpl implements RedisLock {
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public boolean tryLock() {
        Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent("key", "lock", 20L, TimeUnit.SECONDS);
        return false;
    }

    @Override
    public boolean releaseLock() {
        return false;
    }
}
