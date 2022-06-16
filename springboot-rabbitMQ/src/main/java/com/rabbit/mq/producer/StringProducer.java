package com.rabbit.mq.producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**ConfirmCallback   ReturnCallback
 * 使用场景：
 *
 * 如果消息没有到exchange,则confirm回调,ack=false
 *
 * 如果消息到达exchange,则confirm回调,ack=true
 *
 * exchange到queue成功,则不回调return
 *
 * exchange到queue失败,则回调return(需设置mandatory=true,否则不回回调,消息就丢了)
 */

@Component
public class StringProducer implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    private final Logger logger = LoggerFactory.getLogger(StringProducer.class);
    private RabbitTemplate rabbitTemplate;

    /**
     * 构造方法注入rabbitTemplate
     */
    @Autowired
    public StringProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;

        rabbitTemplate.setConfirmCallback(this); //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
        rabbitTemplate.setReturnCallback(this);
    }

    public void sendMsg(String content) {
          CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        //  int i = 1/0;
        //把消息放入ROUTINGKEY_A对应的队列当中去，对应的是队列A
        rabbitTemplate.convertAndSend("string", (Object) content,correlationId);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        logger.info(" 回调id:" + correlationData);
        if (b) {
            logger.info("收到回调，Msg成功发送到broker");
        } else {
            logger.info("收到回调，Msg消息发送失败:" + s);
        }

    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.info("Msg消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message);
    }
}
