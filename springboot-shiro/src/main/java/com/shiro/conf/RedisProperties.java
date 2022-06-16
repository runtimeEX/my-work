package com.shiro.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.conf
 * @Description: TODO
 * @date Date : 2021年06月18日 上午10:44
 */
@Data
@ConfigurationProperties(prefix = "app.redis")
public class RedisProperties {
    /**
     * 是否开启 true/false
     */
    private boolean enabled;
}
