package com.rabbit.mq.listener.direct;

import com.rabbit.mq.conf.RabbitMqConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.PublisherCallbackChannel;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DirectListener {
    private static final Logger log = LoggerFactory.getLogger(DirectListener.class);

    @RabbitListener(queues = RabbitMqConfig.QUEUE_A, containerFactory = "multiListenerContainer")
    // @RabbitHandler
    public void consumeMsgQueue(Message message, Channel channel) throws Exception {
        long tag = message.getMessageProperties().getDeliveryTag();
        String spring_returned_message_correlation = (String) message.getMessageProperties().getHeaders().get(PublisherCallbackChannel.RETURNED_MESSAGE_CORRELATION_KEY);
        try {
          //  int q = 1/0;
            log.info(Thread.currentThread().getName() + "消费:" + new String(message.getBody()));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);//false只确认当前consumer一个消息收到，true确认所有consumer获得的消息。
        } catch (Exception e) {
            // 如果是重复投递的消息，message.getMessageProperties().getRedelivered() 为 true
            if (message.getMessageProperties().getRedelivered()) {
                log.info("消息已重复处理失败,拒绝再次接收...。");
                // 拒绝消息,true则重新入队列，否则丢弃或者进入死信队列
                channel.basicReject(tag, false);
            } else {
                log.info("消息即将再次返回队列处理...");
                //在第一个参数DeliveryTag中如果输入3，则消息DeliveryTag小于等于3的，这个Channel的，都会被拒收
                //第二个参数false只确认当前consumer一个消息收到，true确认所有consumer获得的消息
                // 第三个参数requeue为是否重新回到队列,此处为true，重新回到队列
                channel.basicNack(tag, false, true);
            }
            //必须抛出异常，不然配置文件里配置的重拾次数不生效
         //   throw new Exception();
        }
    }
}
