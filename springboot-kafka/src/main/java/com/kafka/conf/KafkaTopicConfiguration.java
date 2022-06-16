package com.kafka.conf;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-kafka
 * @Package com.kafka.conf
 * @Description: TODO
 * @date Date : 2021年04月15日 下午5:09
 */
@Configuration
public class KafkaTopicConfiguration {
    // 创建一个名为testtopic的Topic并设置分区数为4，分区副本数为2
    // 副本数不能大于kafka节点数
    @Bean
    public NewTopic initialTopic() {
        return new NewTopic("operate_log", 2, (short) 2);
    }


}
