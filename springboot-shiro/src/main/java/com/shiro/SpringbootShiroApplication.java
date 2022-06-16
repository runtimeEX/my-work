package com.shiro;

import com.shiro.utils.parm.ICommonParam;
import com.shiro.utils.parm.impl.BaseCommonParam;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootShiroApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootShiroApplication.class, args);
    }

    @Bean
    public ICommonParam commonParam() {
        return new BaseCommonParam();
    }

}
