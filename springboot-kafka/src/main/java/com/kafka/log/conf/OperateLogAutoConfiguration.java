package com.kafka.log.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.log.aop.AppOperateLogAspect;
import com.kafka.log.filter.LoggingFilter;
import com.kafka.log.handle.ReceiveAppOperateLogHandler;
import com.kafka.log.handle.SendAppOperateLogHandler;
import com.kafka.log.message.DefaultReceiveAppOperateLogMessage;
import com.kafka.log.message.DefaultSendAppOperateLogMessage;
import com.kafka.log.message.ReceiveAppOperateLogMessage;
import com.kafka.log.message.SendAppOperateLogMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.Resource;

@Configuration
@ConditionalOnProperty(prefix = "app.operate.log", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(OperateLogProperties.class)
public class OperateLogAutoConfiguration {

    @Value("${spring.application.name:}")
    private String appName;

    @Autowired
    private OperateLogProperties operateLogProperties;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private KafkaTemplate<Object, Object> template;

    @Bean
    @ConditionalOnMissingBean
    public SendAppOperateLogHandler sendAppOperateLogHandler() {
        return new SendAppOperateLogHandler(operateLogProperties.getTopic(), objectMapper, template);
    }

    @Bean
    @ConditionalOnMissingBean
    public SendAppOperateLogMessage sendAppOperateLogMessage() {
        return new DefaultSendAppOperateLogMessage(new SendAppOperateLogHandler(operateLogProperties.getTopic(), objectMapper, template));
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "app.operate.log.receive", name = "enabled", havingValue = "true", matchIfMissing = true)
    public ReceiveAppOperateLogHandler receiveAppOperateLogHandler() {
        return new ReceiveAppOperateLogHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReceiveAppOperateLogMessage receiveAppOperateLogMessage() {
        return new DefaultReceiveAppOperateLogMessage();
    }

    @Bean
    @ConditionalOnMissingBean
    public AppOperateLogAspect appOperateLogAspect() {
        return new AppOperateLogAspect(appName, operateLogProperties.getModel(), operateLogProperties.getExcludeUri());
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "app.logging",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public FilterRegistrationBean mdcLogServletFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LoggingFilter());
        registration.setName("loggingFilter");
        registration.setOrder(-2147483648);
        return registration;
    }
}