package com.mayikt.lock;

import com.mayikt.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 仅限于单机版本
 *
 */
@Component
public class RedisLock {
    @Autowired
    private RedisUtil redisUtil;


    /**
     * @param lockKey      在Redis中创建的key值
     * @param notLockTimie 尝试获取锁超时时间
     * @return 返回lock成功值
     */
    public boolean getLock(String lockKey, int notLockTimie, int timeOut,String value) {
        boolean b = redisUtil.setNx("boss", 5000, 10000,value);
        return b;
    }

    /**
     * 释放锁
     *
     * @return
     */
    public void unLock(String locKey, String lockValue) {
        if (redisUtil.get(locKey).equals(lockValue)) {
            redisUtil.del(locKey);
        }else {
            System.out.println("不能瞎删别人的锁");
        }
    }
    /**
     *A JVM 获取锁
     *A JVM 执行行业务
     *A JVM 释放锁
     *
     */
}
