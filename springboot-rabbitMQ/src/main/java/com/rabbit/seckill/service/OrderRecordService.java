package com.rabbit.seckill.service;

import com.rabbit.seckill.model.OrderRecord;

public interface OrderRecordService {
    public void saveOrder(OrderRecord orderRecord);
    void updateOrder(String token ,String statu);
}
