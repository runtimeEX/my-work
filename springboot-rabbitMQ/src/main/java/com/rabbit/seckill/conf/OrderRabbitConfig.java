package com.rabbit.seckill.conf;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OrderRabbitConfig {
    // 添加修改库存队列
    public static final String MODIFY_INVENTORY_QUEUE = "modify_inventory_queue";
    // 交换机名称
    public static final String MODIFY_EXCHANGE_NAME = "speckill_exchange";

    // 1.添加交换机队列
    @Bean
    public Queue directModifyInventoryQueue() {
        return new Queue(MODIFY_INVENTORY_QUEUE);
    }

    // 2.定义交换机
    @Bean
    DirectExchange directModifyExchange() {
        return new DirectExchange(MODIFY_EXCHANGE_NAME);
    }

    // 3.修改库存队列绑定交换机
    @Bean
    Binding bindingExchangeintegralDicQueue() {
        return BindingBuilder.bind(directModifyInventoryQueue()).to(directModifyExchange()).with("modifyRoutingKey");
    }

}
