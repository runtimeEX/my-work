package com.boss.wx;

import com.boss.wx.pay.properties.AppWxPayProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppWxPayProperties.class)
@SpringBootApplication
public class SpringbootWxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootWxApplication.class, args);
    }

}
