package com.rabbit.seckill.service.impl;

import com.rabbit.seckill.mapper.OrderRecordMapper;
import com.rabbit.seckill.model.OrderRecord;
import com.rabbit.seckill.service.OrderRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderRecordServiceImpl implements OrderRecordService {
    @Autowired
    private OrderRecordMapper orderRecordMapper;

    @Override
    public void saveOrder(OrderRecord orderRecord) {
        orderRecordMapper.insert(orderRecord);
    }

    @Override
    public void updateOrder(String token, String statu) {
        orderRecordMapper.updateByToken(token,statu);
    }
}
