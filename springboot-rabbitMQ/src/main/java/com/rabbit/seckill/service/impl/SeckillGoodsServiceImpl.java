package com.rabbit.seckill.service.impl;

import com.rabbit.seckill.mapper.SeckillGoodsMapper;
import com.rabbit.seckill.model.SeckillGoods;
import com.rabbit.seckill.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SeckillGoodsServiceImpl implements SeckillGoodsService {
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Override
    public int reduceStock(long goodsId) {
        return seckillGoodsMapper.updateStock(goodsId);
    }

    @Override
    public SeckillGoods find(long goodsId) {
        return seckillGoodsMapper.select(goodsId);
    }

    @Override
    public int inventory(Long seckillId) {
        return seckillGoodsMapper.updateStock(seckillId);
    }

    @Override
    public int insert(SeckillGoods record) {
        return seckillGoodsMapper.insert(record);
    }
}
