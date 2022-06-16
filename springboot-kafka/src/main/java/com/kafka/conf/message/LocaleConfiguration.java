package com.kafka.conf.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-kafka
 * @Package com.kafka.conf
 * @Description: TODO
 * @date Date : 2021年05月08日 下午2:56
 */
@Configuration
@EnableConfigurationProperties(LocaleProperties.class)
public class LocaleConfiguration implements WebMvcConfigurer {
    @Autowired
    private LocaleProperties localeProperties;
    /**
     * 国际化解析器BEAN，注意这个Bean的名称必须为localeResolver
     */
    @Bean(name = "localeResolver")
    public LocaleResolver initLocaleResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        // 设置默认的国际化区域
        sessionLocaleResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return sessionLocaleResolver;
    }

    //拦截参数上带有language的请求
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        registry.addInterceptor(localeChangeInterceptor);
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        if (null != localeProperties.getBasenames()) {
            messageSource.setBasenames(localeProperties.getBasenames());
        } else {
            String[] defaultBasenames = {
                    "classpath:i18n/messages",
                    "classpath:i18n/validatorMessages",
                    "classpath:i18n/commonMessages"
            };

            messageSource.setBasenames(defaultBasenames);
        }

        //多级刷新一次配置文件
        messageSource.setCacheSeconds(localeProperties.getCacheSeconds()); //refresh cache once per hour
        messageSource.setDefaultEncoding(localeProperties.getDefaultEncoding());
        return messageSource;
    }
    @Bean
    public LocaleService localeService() {
        return new LocaleService(messageSource());
    }


}
