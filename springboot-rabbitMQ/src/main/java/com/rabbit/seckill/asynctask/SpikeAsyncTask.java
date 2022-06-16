package com.rabbit.seckill.asynctask;

import com.alibaba.fastjson.JSONObject;
import com.rabbit.seckill.producer.OrderProducer;
import com.rabbit.seckill.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-rabbitMQ
 * @Package com.rabbit.seckill.asynctask
 * @Description: TODO
 * @date Date : 2021年07月13日 上午11:55
 */
@Component
public class SpikeAsyncTask {
    @Autowired
    private OrderProducer orderProducer;
    @Autowired
    private RedisUtil redisUtil;
    @Async
    public void createSeckillToken(Long seckillId, Long tokenQuantity) {
        List<String> listToken = new ArrayList<>();
        for (int i = 0; i < tokenQuantity; i++) {
            String token = "seckill_" + i;
            listToken.add(token);
        }
        redisUtil.lSet(seckillId + "", listToken);

    }
    /**
     * 获取到秒杀token之后，异步放入mq中实现修改商品的库存
     */
    @Async
    public void sendSeckillMsg(Long seckillId, String token) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("seckillId", seckillId);
        jsonObject.put("token", token);
        orderProducer.sendMsg(jsonObject);
    }

}
