package com.rabbit.mq.listener;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StringMqListener {
    private static final Logger log = LoggerFactory.getLogger(StringMqListener.class);

    @RabbitListener(queues = "string", containerFactory = "singleListenerContainer")
    // @RabbitHandler
    public void consumeStringQueue(Message message, Channel channel) throws IOException {
       // int i = 1/0;
        try {
            log.info(Thread.currentThread().getName() + "消费:" + new String(message.getBody()));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);//false只确认当前consumer一个消息收到，true确认所有consumer获得的消息。
        } catch (Exception e) {
            if (message.getMessageProperties().getRedelivered()) {
                System.out.println("消息已重复处理失败,拒绝再次接收...");
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false); // 拒绝消息
            } else {
                System.out.println("消息即将再次返回队列处理...");
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true); // requeue为是否重新回到队列
            }
        }

    }
}
