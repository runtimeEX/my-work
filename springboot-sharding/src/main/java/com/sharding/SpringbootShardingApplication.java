package com.sharding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-sharding
 * @Package com.sharding
 * @Description: TODO
 * @date Date : 2022年07月14日 下午3:55
 */
@MapperScan
@SpringBootApplication
public class SpringbootShardingApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootShardingApplication.class, args);
    }
}
