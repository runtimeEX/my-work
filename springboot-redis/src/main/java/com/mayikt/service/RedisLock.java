package com.mayikt.service;

public interface RedisLock {

    /**
     * 获取锁
     *
     * @return
     */
    boolean tryLock();

    /**
     * 释放锁
     *
     * @return
     */
    boolean releaseLock();
}
