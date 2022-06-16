package com.shiro.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.conf
 * @Description: TODO
 * @date Date : 2021年06月18日 上午10:20
 */
@Data
@ConfigurationProperties(prefix = "app.shiro")
public class ShiroProperties {
    /**
     * 是否开启shiro配置
     */
    private boolean enabled;
    /**
     * 缓存超时时间，单位秒
     */
    private int cacheExp = 60 * 60 * 24;
    /**
     * 过滤
     */
    private Map<String, String> filterChainDefinitionMap = new HashMap<>();
}
