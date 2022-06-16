package com.rabbit.mq.listener.topic;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * 第一种写法， TopicListenerA，TopicListenerB为第二种写法
 */
@Component("topicListener")
public class TopicListener implements ChannelAwareMessageListener {
    private static final Logger log = LoggerFactory.getLogger(TopicListener.class);

    /**
     * basicReject一次只能拒绝接收一个消息，而basicNack方法可以支持一次0个或多个消息的拒收，并且也可以设置是否requeue。
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long tag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info(Thread.currentThread().getName() + "消费:" + new String(message.getBody()));
          //  int a = 1/0;
            channel.basicAck(tag, false);//false只确认当前consumer一个消息收到，true确认所有consumer获得的消息。
        } catch (Exception e) {
            // 如果是重复投递的消息，message.getMessageProperties().getRedelivered() 为 true
            if (message.getMessageProperties().getRedelivered()) {
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
