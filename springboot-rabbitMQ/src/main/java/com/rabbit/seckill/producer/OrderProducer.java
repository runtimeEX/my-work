package com.rabbit.seckill.producer;

import com.alibaba.fastjson.JSONObject;
import com.rabbit.mq.conf.RabbitMqConfig;
import com.rabbit.seckill.conf.OrderRabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderProducer implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    private final Logger logger = LoggerFactory.getLogger(OrderProducer.class);
    //由于rabbitTemplate的scope属性设置为ConfigurableBeanFactory.SCOPE_PROTOTYPE，所以不能自动注入
    private RabbitTemplate rabbitTemplate;

    public void sendMsg(JSONObject content) {
        CorrelationData correlationId = new CorrelationData(content.toJSONString());
        rabbitTemplate.convertAndSend(OrderRabbitConfig.MODIFY_EXCHANGE_NAME, "modifyRoutingKey", content, correlationId);

    }

    /**
     * 构造方法注入rabbitTemplate
     */
    @Autowired
    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this); //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
        rabbitTemplate.setReturnCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info(" 回调id:" + correlationData);
        if (ack) {
            logger.info("收到回调，Msg成功发送到broker");
        } else {
            logger.info("收到回调，Msg消息发送失败:" + cause);
        }

    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.info("Msg消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message);
    }

}
