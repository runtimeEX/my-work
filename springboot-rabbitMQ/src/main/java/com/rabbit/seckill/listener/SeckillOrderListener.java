package com.rabbit.seckill.listener;

import com.alibaba.fastjson.JSONObject;
import com.rabbit.mq.producer.DelayProducer;
import com.rabbit.seckill.conf.OrderRabbitConfig;
import com.rabbit.seckill.model.OrderRecord;
import com.rabbit.seckill.model.SeckillGoods;
import com.rabbit.seckill.service.OrderRecordService;
import com.rabbit.seckill.service.SeckillGoodsService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class SeckillOrderListener {

    private static final Logger log = LoggerFactory.getLogger(SeckillOrderListener.class);


    @Autowired
    private SeckillGoodsService seckillGoodsService;
    @Autowired
    private OrderRecordService orderRecordService;
    @Autowired
    private DelayProducer delayProducer;

    // 调用数据库层判断
    public Boolean toDaoResult(int result) {
        return result > 0 ? true : false;
    }

    @Transactional
    @RabbitListener(queues = OrderRabbitConfig.MODIFY_INVENTORY_QUEUE, containerFactory = "singleListenerContainer")
    public void consumeMsgQueue(Message message, Channel channel) throws Exception {
        long tag = message.getMessageProperties().getDeliveryTag();

        String messageId = message.getMessageProperties().getMessageId();
        String msg = new String(message.getBody(), "UTF-8");
        log.info(">>>messageId:{},msg:{}", messageId, msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        // 1.获取秒杀id
        Long seckillId = jsonObject.getLong("seckillId");
        SeckillGoods seckillGoods = seckillGoodsService.find(seckillId);
        if (seckillGoods == null) {
            log.warn("seckillId:{},商品信息不存在!", seckillId);
            return;
        }
        //2.减库存
        int inventory = seckillGoodsService.inventory(seckillId);
        if (!toDaoResult(inventory)) {
            log.info(">>>seckillId:{}修改库存失败>>>>inventory返回为{} 秒杀失败！", seckillId, inventory);
            return;
        }
        // 3.添加秒杀订单
        OrderRecord orderRecord = new OrderRecord();
        orderRecord.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        orderRecord.setOrderType("1");
        orderRecord.setStatu("未支付");
        orderRecord.setToken(jsonObject.getString("token"));
        orderRecordService.saveOrder(orderRecord);
        delayProducer.sendMsg(jsonObject);
        try {
            channel.basicAck(tag, false);//false只确认当前consumer一个消息收到，true确认所有consumer获得的消息。
        } catch (Exception e) {
            // 如果是重复投递的消息，message.getMessageProperties().getRedelivered() 为 true
                log.info("消息已重复处理失败,拒绝再次接收...。");
                // 拒绝消息,requeue=false 表示不再重新入队
                channel.basicReject(tag, false);
        }
    }
}
