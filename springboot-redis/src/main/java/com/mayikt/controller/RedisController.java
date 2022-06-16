package com.mayikt.controller;

import com.mayikt.entity.Goods;
import com.mayikt.entity.UserEntity;
import com.mayikt.lock.RedisLock;
import com.mayikt.mapper.GoodsMapper;
import com.mayikt.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class RedisController {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private RedisLock redisLock;
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @GetMapping("/addUser")
    public String addUser(UserEntity userEntity) {
        redisUtil.set("user", userEntity);
        return "success";
    }

    @GetMapping("/getUser")
    public UserEntity getUser(String key) {
        UserEntity o = (UserEntity) redisUtil.get(key);
        return o;

    }

    @GetMapping("/getListGoods")
    @Cacheable(cacheNames = "goods", key = "'getListGoods'")
    public List<Goods> getListGoods() {
        List<Goods> goodsList = goodsMapper.findAll();
        return goodsList;
    }

    @GetMapping("/lock")
    public String lock() {
        String value = UUID.randomUUID().toString();
        boolean lock = redisLock.getLock("boss", 5000, 100000, value);
        if (!lock) {

            System.out.println("获取锁失败，释放锁");
            redisLock.unLock("boss", value);
        }

        //
        return "aaa";
    }

    @RequestMapping("/test")
    public String redisTest() {
        synchronized (atomicInteger){
            System.out.println(this);
            Integer kucun = (Integer) redisUtil.get("kucun");
            if (kucun > 0) {
                redisUtil.set("kucun", kucun - 1);
                System.out.println("剩余库存：" + redisUtil.get("kucun") + ":" + atomicInteger.incrementAndGet());
            } else {
                System.out.println("库存不足:" + kucun);
            }
        }

        return "aaaa";
    }
}
