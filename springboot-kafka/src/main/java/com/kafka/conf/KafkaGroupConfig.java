package com.kafka.conf;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-kafka
 * @Package com.kafka.conf
 * @Description: TODO
 * @date Date : 2022年06月27日 上午10:32
 */
@Configuration
public class KafkaGroupConfig implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.setProperty("app.group", InetAddress.getLocalHost().getHostAddress());
    }
}
