package com.rabbit.seckill.controller;

import com.rabbit.seckill.asynctask.SpikeAsyncTask;
import com.rabbit.seckill.model.SeckillGoods;
import com.rabbit.seckill.service.SeckillGoodsService;
import com.rabbit.seckill.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/seckill")
public class SeckillGoodsController {
    private static final Logger log = LoggerFactory.getLogger(SeckillGoodsController.class);
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SpikeAsyncTask spikeAsyncTask;

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    /**
     * 生成令牌桶
     *
     * @return
     */
    @RequestMapping("/addSpikeToken")
    public String addSpikeToken(Long seckillId, Long tokenQuantity) {

        //添加秒杀商品
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setSeckilPrice(BigDecimal.valueOf(200.98));
        seckillGoods.setStockCount(tokenQuantity);
        seckillGoods.setGoodsId(seckillId);
        seckillGoodsService.insert(seckillGoods);
        spikeAsyncTask.createSeckillToken(seckillId, tokenQuantity);
        return "success";
    }

    /**
     * @param seckillId 秒杀商品id
     * @return
     */
    @GetMapping("/spike")
    public String spike(Long seckillId) {
        //从redis从获取对应的秒杀token
        String seckillToken = redisUtil.getListKeyToken(seckillId + "");
        if (StringUtils.isEmpty(seckillToken)) {
            log.info(">>>seckillId:{}, 亲，该秒杀已经售空，请下次再来!", seckillId);
            return "亲，该秒杀已经售空，请下次再来!";
        }
//.获取到秒杀token之后，异步放入mq中实现修改商品的库存
        spikeAsyncTask.sendSeckillMsg(seckillId, seckillToken);
        return "success";
    }

}
