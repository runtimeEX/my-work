package com.rabbit.seckill.service;

import com.rabbit.seckill.model.SeckillGoods;

public interface SeckillGoodsService {
    int reduceStock(long goodsId);
    SeckillGoods find(long goodsId);

    int inventory(Long seckillId);
    int insert(SeckillGoods record);
}
