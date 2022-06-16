package com.shiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiro.utils.parm.ICommonParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro
 * @Description: TODO
 * @date Date : 2022年06月15日 上午9:54
 */
@Configuration
public class WebAutoConfiguration implements WebMvcConfigurer {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ICommonParam commonParam;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ContextInterceptor()).addPathPatterns("/**").order(-99);
        registry.addInterceptor(new CommonParamInterceptor(commonParam, objectMapper)).addPathPatterns("/**").order(-50);

    }
}
