package com.rabbit.seckill.controller;

import com.rabbit.seckill.model.SeckillGoods;
import com.rabbit.seckill.util.RedisUtil;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    private RedisUtil redisUtil;
  /*  @Autowired
    private JedisUtil jedisUtil;*/
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private Redisson redisson;


    @RequestMapping("/set")
    public boolean redisset(String key, String value) {
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setSeckilPrice(BigDecimal.valueOf(100.44));
        redisUtil.set(key, seckillGoods, 0);

        redisUtil.set(key,value);
       // jedisUtil.set(14, SerializeUtil.serialize(key), SerializeUtil.serialize(value), 0);
        return true;
    }

    /**
     * 单机情况下，可以用这个锁解决超卖问题
     *
     * @return
     */
    @RequestMapping("/test")
    public String redisTest() {
        synchronized (this) {
            System.out.println(this);
            Integer kucun = (Integer) redisUtil.get("kucun");
            if (kucun > 0) {
                redisUtil.set("kucun", kucun - 1);
                System.out.println("剩余库存：" + redisUtil.get("kucun"));
            } else {
                System.out.println("库存不足");
            }
            return "success";
        }
    }

    /**
     * 分布式服务解决超卖  分布式锁
     *
     * @return
     */
    @RequestMapping("/redislock")
    public String redisClusterTest() {
        String lock = "lock";
        String id = UUID.randomUUID().toString();//防止一个请求时间过长，锁已经失效，最后却删了其他请求的锁，导致setnx无效
        try {
            Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent(lock, id, 10, TimeUnit.SECONDS);
            if (!ifAbsent) {
                return "锁住了，等下再试试";
            }
            Integer kucun = (Integer) redisUtil.get("kucun");
            if (kucun > 0) {
                redisUtil.set("kucun", kucun - 1);
                System.out.println("剩余库存：" + redisUtil.get("kucun"));
            } else {
                System.out.println("库存不足");
            }
        } finally {
            if (id.equals(redisUtil.get(lock))) {
                redisUtil.del(lock);
            }
        }

        return "success";
    }

    /**
     * redisson
     * @return
     */
    @RequestMapping("/redisson")
    public String redissonTest() {
        String lock = "lock";
        RLock redissonLock = redisson.getLock(lock);
        //  String id = UUID.randomUUID().toString();//防止一个请求时间过长，锁已经失效，最后却删了其他请求的锁，导致setnx无效
        try {
          /*  Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent(lock, id, 10, TimeUnit.SECONDS);
            if (!ifAbsent) {
                return "锁住了，等下再试试";
            }*/
          //加锁
          redissonLock.lock(10,TimeUnit.SECONDS);
            Integer kucun = (Integer) redisUtil.get("kucun");
            if (kucun > 0) {
                redisUtil.set("kucun", kucun - 1);
                System.out.println("剩余库存：" + redisUtil.get("kucun"));
            } else {
                System.out.println("库存不足");
            }
        } finally {
            redissonLock.unlock();
           /* if (id.equals(redisUtil.get(lock))) {
                redisUtil.del(lock);
            }*/
        }

        return "success";
    }

}
