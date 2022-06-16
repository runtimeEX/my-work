package com.rabbit.jpush;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 极光对接配置
 *
 * @author wangshaojun
 * @since 2021-05-14
 */
@ConfigurationProperties(prefix = "app.jpush-message")
public class JPushProperties {

    private String appKey;
    private String masterSecret;
    //#向某单个设备或者某设备列表推送一条通知、或者消息接口地址。POST
    private String pushUrl;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getMasterSecret() {
        return masterSecret;
    }

    public void setMasterSecret(String masterSecret) {
        this.masterSecret = masterSecret;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }
}
