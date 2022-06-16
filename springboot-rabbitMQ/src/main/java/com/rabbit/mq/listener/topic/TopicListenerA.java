package com.rabbit.mq.listener.topic;

import com.rabbit.mq.conf.RabbitMqConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TopicListenerA {
    private static final Logger log = LoggerFactory.getLogger(TopicListenerA.class);

    @RabbitListener(queues = RabbitMqConfig.QUEUE_B, containerFactory = "multiListenerContainer")
    // @RabbitHandler
    public void consumeMsgQueue(Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info(Thread.currentThread().getName() + "A消费:" + new String(message.getBody()));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);//false只确认当前consumer一个消息收到，true确认所有consumer获得的消息。
        } catch (Exception e) {
            // 如果是重复投递的消息，message.getMessageProperties().getRedelivered() 为 true
            Boolean redelivered = message.getMessageProperties().getRedelivered();

            if (redelivered) {
                log.info("消息已重复处理失败,拒绝再次接收...");
                // 拒绝消息,requeue=false 表示不再重新入队
                channel.basicReject(tag, false);
            } else {
                log.info("消息即将再次返回队列处理...");
                //在第一个参数DeliveryTag中如果输入3，则消息DeliveryTag小于等于3的，这个Channel的，都会被拒收
                //第二个参数false只确认当前consumer一个消息收到，true确认所有consumer获得的消息
                // 第三个参数requeue为是否重新回到队列,此处为true，重新回到队列
                channel.basicNack(tag, false, true);
            }
        }
    }
}
