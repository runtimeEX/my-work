package com.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-kafka
 * @Package com.kafka.consumer
 * @Description: TODO
 * @date Date : 2021年04月15日 下午5:01
 */
@Component
public class Consumer {
    //  @KafkaListener(topics = {"boss"},containerFactory = "containerFactory")
    @KafkaListener(topicPartitions = {@TopicPartition(topic = "boss", partitions = {"0", "3"}, partitionOffsets = {@PartitionOffset(partition = "2", initialOffset = "1")})}, containerFactory = "containerFactory")
    public void getMessage(ConsumerRecord<?, ?> record, Acknowledgment acknowledgment) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println("简单消费：" + record.topic() + "-" + record.partition() + "-" + record.offset() + "-" + record.key() + "-" + record.value());
        acknowledgment.acknowledge();
    }

    @KafkaListener(groupId = "one", topics = {"topic1"}, containerFactory = "containerFactory")
    public void getMessage1(ConsumerRecord<?, ?> record, Acknowledgment acknowledgment) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println("简单消费：" + record.topic() + "-" + record.partition() + "-" + record.key() + "-" + record.value());
        acknowledgment.acknowledge();
    }


    @KafkaListener(id = "consumer1", groupId = "felix-group",
            topicPartitions = {@TopicPartition(topic = "boss", partitions = {"0", "1"},
                    partitionOffsets = {@PartitionOffset(partition = "3", initialOffset = "2")}),
                    @TopicPartition(topic = "topic1", partitions = "0")}, containerFactory = "containerFactory")
    public void onMessage2(ConsumerRecord<?, ?> record, Acknowledgment acknowledgment) {
        System.out.println("topic:" + record.topic() + "|partition:" + record.partition() + "|offset:" + record.offset() + "|value:" + record.value());
        acknowledgment.acknowledge();
    }

    @KafkaListener(id = "consumer2", groupId = "batch", topics = "topic1", containerFactory = "batchFactory")
    public void onMessage3(List<ConsumerRecord<?, ?>> records, Acknowledgment acknowledgment) throws Exception {
        System.out.println(">>>批量消费一次，records.size()=" + records.size());
        for (ConsumerRecord<?, ?> record : records) {
            System.out.println(record.value());
        }

        acknowledgment.acknowledge();
        throw new Exception("批量消费-模拟异常");
    }

    /**
     * 多消费者批量消费
     */
    @KafkaListener(id = "batch1",groupId = "btc",topics = "topic1",containerFactory = "batchFactory")
    public void batch1(List<ConsumerRecord<?, ?>> records,Acknowledgment acknowledgment) {
        System.out.println(">>>batch1批量消费，records.size()=" + records.size());
        for (ConsumerRecord<?, ?> record : records) {
            System.out.println("batch1topic:" + record.topic() + "|partition:" + record.partition() + "|offset:" + record.offset() + "|value:" + record.value());
        }
        acknowledgment.acknowledge();
    }
    @KafkaListener(id = "batch2",groupId = "btc",topics = "topic1",containerFactory = "batchFactory")
    public void batch2(List<ConsumerRecord<?, ?>> records,Acknowledgment acknowledgment) {
        System.out.println(">>>batch2批量消费，records.size()=" + records.size());
        for (ConsumerRecord<?, ?> record : records) {
            System.out.println("batch2topic:" + record.topic() + "|partition:" + record.partition() + "|offset:" + record.offset() + "|value:" + record.value());
        }
        acknowledgment.acknowledge();
    }
}
