package com.mayikt.utils;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisLockUtil {
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 加锁
     *
     * @param key           锁的 key
     * @param value         value （ key + value 必须保证唯一）
     * @param expire        key 的过期时间，单位 ms
     * @param retryTimes    重试次数，即加锁失败之后的重试次数
     * @param retryInterval 重试时间间隔，单位 ms
     * @return 加锁 true 成功
     */
    public RLock getLock(String key, String value, long expire, int retryTimes, long retryInterval) {
        log.info("locking... redisK = {}", key);
        RLock fairLock = redissonClient.getFairLock(key + ":" + value);
        try {
            boolean tryLock = fairLock.tryLock(0, expire, TimeUnit.MILLISECONDS);
            if (tryLock) {
                log.info("locked... redisK = {}", key);
                return fairLock;
            } else {
                //重试获取锁
                log.info("retry to acquire lock: [redisK = {}]", key);
                int count = 0;
                while (count < retryTimes) {
                    try {
                        Thread.sleep(retryInterval);
                        tryLock = fairLock.tryLock(0, expire, TimeUnit.MILLISECONDS);
                        if (tryLock) {
                            log.info("locked... redisK = {}", key);
                            return fairLock;
                        }
                        log.warn("{} times try to acquire lock", count + 1);
                        count++;
                    } catch (Exception e) {
                        log.error("acquire redis occurred an exception", e);
                        break;
                    }
                }
                log.info("fail to acquire lock {}", key);
            }
        } catch (Throwable e1) {
            log.error("acquire redis occurred an exception", e1);
        }

        return fairLock;
    }

    /**
     * 加锁
     *
     * @param key           锁的 key
     * @param value         value （ key + value 必须保证唯一）
     * @param expire        key 的过期时间，单位 ms
     * @param retryTimes    重试次数，即加锁失败之后的重试次数
     * @param retryInterval 重试时间间隔，单位 ms
     * @return 加锁 true 成功
     */
    public boolean lock(String key, String value, long expire, int retryTimes, long retryInterval) {
        log.info("locking... redisK = {}", key);
        RLock fairLock = redissonClient.getFairLock(key + ":" + value);
        try {
            boolean tryLock = fairLock.tryLock(0, expire, TimeUnit.MILLISECONDS);
            fairLock.lock();
            if (tryLock) {
                log.info("locked... redisK = {}", key);
                return true;
            } else {
                //重试获取锁
                log.info("retry to acquire lock: [redisK = {}]", key);
                int count = 0;
                while (count < retryTimes) {
                    try {
                        Thread.sleep(retryInterval);
                        tryLock = fairLock.tryLock(0, expire, TimeUnit.MILLISECONDS);
                        if (tryLock) {
                            log.info("locked... redisK = {}", key);
                            return true;
                        }
                        log.warn("{} times try to acquire lock", count + 1);
                        count++;
                    } catch (Exception e) {
                        log.error("acquire redis occurred an exception", e);
                        break;
                    }
                }

                log.info("fail to acquire lock {}", key);
                return false;
            }
        } catch (Throwable e1) {
            log.error("acquire redis occurred an exception", e1);
            return false;
        }
    }

    /**
     * 释放KEY
     *
     * @return 释放锁 true 成功
     */
    public boolean unlock(String key, String value) {
        RLock fairLock = redissonClient.getFairLock(key + ":" + value);
        try {
            //如果这里抛异常，后续锁无法释放
            if (fairLock.isLocked()) {
                fairLock.unlock();
                log.info("release lock success");
                return true;
            }
        } catch (Throwable e) {
            log.error("release lock occurred an exception", e);
        }

        return false;
    }


}
