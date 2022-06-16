/*
package com.rabbit.seckill.controller;

import com.rabbit.seckill.util.JedisClusterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jedisCluster")
public class JedisClusterController {
    @Autowired
    private JedisClusterUtil jedisClusterUtil;

    @GetMapping("/set")
    public String setValue() {
        System.out.println("jiqunle");
        String s = jedisClusterUtil.set("jedis", "cluster");
        return s;
    }

    @GetMapping("/get")
    public String getValue() {
        String s = jedisClusterUtil.get("jedis");
        return s;
    }
}
*/
