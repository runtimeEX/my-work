/*
package com.rabbit.seckill.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class JedisUtil {
    @Autowired
    private JedisPool jedisPool;


    public void returnResource(Jedis jedis, boolean isBroken) {
        if (jedis == null){
            return;
        }
        if (isBroken==false){
            jedis.close();
        }
    }

    public Jedis getJedis() {
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }

    public boolean set(int dbIndex, byte[] key, byte[] value, int expireTime) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            jedis.set(key, value);
            if (expireTime > 0) {
                jedis.expire(key, expireTime);
            }
        } catch (Exception e) {
            isBroken = true;
            return false;
        } finally {
            returnResource(jedis, isBroken);
        }
        return true;
    }

}
*/
