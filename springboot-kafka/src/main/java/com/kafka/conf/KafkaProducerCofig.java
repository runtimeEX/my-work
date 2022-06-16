package com.kafka.conf;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-kafka
 * @Package com.kafka.conf
 * @Description: TODO
 * @date Date : 2021年04月16日 下午4:00
 */
@Configuration
public class KafkaProducerCofig {
    /**
     * Producer 参数配置
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        // 指定多个kafka集群多个地址
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "runtimex.vip:9092");
        //开启幂等性功能
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        // 重试次数，0为不启用重试机制
        props.put(ProducerConfig.RETRIES_CONFIG, 10);
        //同步到副本, 默认为1
        // acks=0 把消息发送到kafka就认为发送成功
        // acks=1 把消息发送到kafka leader分区，并且写入磁盘就认为发送成功
        // acks=all 把消息发送到kafka leader分区，并且leader分区的副本follower对消息进行了同步就任务发送成功
        //使用事务时，必须设置为all
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        // 生产者空间不足时，send()被阻塞的时间，默认60s
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 6000);
        //当生产端积累的消息达到batch-size或接收到消息linger.ms后,生产者就会将消息提交给kafka
        // linger.ms为0表示每接收到一条消息就提交给kafka,这时候batch-size其实就没用了
        // 控制批处理大小，单位为字节
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 4096);
        // 批量发送，延迟为10毫秒，启用该功能能有效减少生产者发送消息次数，从而提高并发量（异步模式下）
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        // 生产者可以使用的总内存字节来缓冲等待发送到服务器的记录
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 40960);
        // 消息的最大大小限制,也就是说send的消息大小不能超过这个限制, 默认1048576(1MB)
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 1048576);
        // 键的序列化方式
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 值的序列化方式
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 压缩消息，支持四种类型，分别为：none、lz4、gzip、snappy，默认为none。
        // 消费者默认支持解压，所以压缩设置在生产者，消费者无需设置。
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "none");
        return props;
    }

    /**
     *
     *
     * @return
     */
    @Bean
    public ProducerFactory producerFactory() {
        DefaultKafkaProducerFactory factory = new DefaultKafkaProducerFactory<>(producerConfigs());
        //在producerFactory中开启事务功能
      //  factory.transactionCapable();

        //TransactionIdPrefix是用来生成Transactional.id的前缀
        factory.setTransactionIdPrefix("tran-");
        return factory;

    }

    /**
     * 开启kafka事务
     * @return
     */
    @Bean
    public KafkaTransactionManager transactionManager() {
        KafkaTransactionManager manager = new KafkaTransactionManager(producerFactory());
        return manager;
    }
}
