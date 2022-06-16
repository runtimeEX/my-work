package com.kafka.conf;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.BatchErrorHandler;
import org.springframework.kafka.listener.ConsumerAwareErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-kafka
 * @Package com.kafka.conf
 * @Description: TODO
 * @date Date : 2021年04月16日 上午10:21
 */
@Configuration
public class KafkaConsumerConfig {
    @Bean
    KafkaListenerContainerFactory<?> batchFactory(ConsumerFactory consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        // 设置消费者工厂
        factory.setConsumerFactory(consumerFactory);
        // 消费者组中线程数量
        factory.setConcurrency(8);
        // 拉取超时时间
        factory.getContainerProperties().setPollTimeout(30000);
        // 当使用批量监听器时需要设置为true
        factory.setBatchListener(true);
        //设置手动提交ackMode
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        // 将批量消息异常处理器添加到参数中
        factory.setBatchErrorHandler(new BatchErrorHandler() {
            @Override
            public void handle(Exception thrownException, ConsumerRecords<?, ?> record) {
                record.forEach(new java.util.function.Consumer<ConsumerRecord<?, ?>>() {
                    @Override
                    public void accept(ConsumerRecord<?, ?> consumerRecord) {
                        System.out.println("// 将批量消息异常");
                        System.out.println("topic:" + consumerRecord.topic() + "|partition:" + consumerRecord.partition() + "|offset:" + consumerRecord.offset() + "|value:" + consumerRecord.value());
                    }
                });

            }
        });
        // 被过滤的消息将被丢弃
        factory.setAckDiscarded(true);
        //设置消息过滤 返回true的消息将被过滤
        factory.setRecordFilterStrategy(new RecordFilterStrategy<String, String>() {
            @Override
            public boolean filter(ConsumerRecord<String, String> consumerRecord) {

                return false;
            }
        });
        return factory;
    }

    @Bean
    KafkaListenerContainerFactory<?> containerFactory(ConsumerFactory consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        // 设置消费者工厂
        factory.setConsumerFactory(consumerFactory);
        // 消费者组中线程数量(设置大小与分区数相等)
        factory.setConcurrency(4);
        // 拉取超时时间
        factory.getContainerProperties().setPollTimeout(30000);
        // 当使用批量监听器时需要设置为true
        factory.setBatchListener(false);
        //设置手动提交ackMode
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        // 将单条消息异常处理器添加到参数中
        factory.setErrorHandler(new ConsumerAwareErrorHandler() {
            @Override
            public void handle(Exception thrownException, ConsumerRecord<?, ?> data, Consumer<?, ?> consumer) {
                System.out.println("// 将单条消息异常");
            }
        });
        return factory;
    }
    /**
     * AckMode模式	    作用
     * MANUAL	        当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后, 手动调用Acknowledgment.acknowledge()后提交
     * MANUAL_IMMEDIATE	手动调用Acknowledgment.acknowledge()后立即提交
     * RECORD	        当每一条记录被消费者监听器（ListenerConsumer）处理之后提交
     * BATCH	        当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后提交
     * TIME	            当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后，距离上次提交时间大于TIME时提交
     * COUNT	        当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后，被处理record数量大于等于COUNT时提交
     * COUNT_TIME	    TIME或COUNT　有一个条件满足时提交
     * ————————————————
     * MANUAL: 在处理完最后一次轮询的所有结果后，将队列排队，并在一次操作中提交偏移量。可以认为是在批处理结束时提交偏移量
     * MANUAL_IMMEDIATE：只要在侦听器线程上执行确认，就立即提交偏移。会在批量执行的时候逐一提交它们。
     */
  /*  @Bean
    public ConsumerFactory consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        // Kafka地址
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "");
        //配置默认分组，这里没有配置+在监听的地方没有设置groupId，多个服务会出现收到相同消息情况
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, "defaultGroup");
        // 是否自动提交offset偏移量(默认true)
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        // 自动提交的频率(ms)
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        // Session超时设置
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        // 键的反序列化方式
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // 值的反序列化方式
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // offset偏移量规则设置：
        // (1)、earliest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
        // (2)、latest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
        // (3)、none：topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return propsMap;
    }*/
}
