package com.sharding;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-sharding
 * @Package com.sharding
 * @Description: TODO
 * @date Date : 2022年07月14日 下午3:55
 */
@MapperScan(basePackages = {"**.mapper","**.repository"})
@SpringBootApplication
public class SpringbootShardingApplication {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        paginationInterceptor.setLimit(50000);
        return paginationInterceptor;
    }
    public static void main(String[] args) {
        SpringApplication.run(SpringbootShardingApplication.class, args);
    }
}
