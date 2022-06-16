package com.kafka.log.conf;

import com.google.common.collect.Sets;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@ConfigurationProperties(prefix = "app.operate.log")
@Data
public class OperateLogProperties {
    /**
     * 是否开启 true/false
     */
    private boolean enabled;

    /**
     * 操作日志模式，GLOBAL 所有的都记录日志， PART 只有使用注解的日志
     */
    private String model = "GLOBAL";

    /**
     * 该设置路径地址，都不记录任何操作日志, 如果路径里包含 注解，则还是会用注解
     */
    private Set<String> excludeUri = Sets.newHashSet();

    /**
     * topic名称
     */
    private String topic;

    /**
     * 消费操作日志
     */
    private Receive receive;

    /**
     * 操作日志模式，GLOBAL 所有的都记录日志， PART 只有使用注解的日志
     */
    public static enum Model {
        GLOBAL, PART
    }

    @Data
    public static class Receive {
        /**
         * 是否开启 true/false
         */
        private boolean enabled;
    }
}