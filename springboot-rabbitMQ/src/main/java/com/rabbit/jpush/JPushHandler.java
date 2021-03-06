package com.rabbit.jpush;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.druid.util.StringUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;

@Slf4j
@Component
public class JPushHandler {
    private Gson gson = new Gson();

    @Autowired
    private JPushProperties jPushProperties;


    public static PushPayload buildPushObject_all_alias_alert_android(PushMessage pushRequest) {
        HashMap<String, String> extrasMap = new HashMap<>();
        extrasMap.put("messageId", pushRequest.getMessageId());
        extrasMap.put("type", "MSG");
        boolean apnsProduction = true;
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias(pushRequest.getPhoneList()))
                .setNotification(Notification.android(pushRequest.getMessage(), pushRequest.getTitle(), extrasMap))
                .setOptions(Options.newBuilder().setApnsProduction(apnsProduction).setTimeToLive(86000).build())
                .build();
    }

    public static PushPayload buildPushObject_all_alias_alert_ios(PushMessage pushRequest) {
        HashMap<String, String> extrasMap = new HashMap<>();
        extrasMap.put("messageId", pushRequest.getMessageId());
        extrasMap.put("type", "MSG");
        boolean apnsProduction = true;
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.alias(pushRequest.getPhoneList()))
                .setNotification(Notification.ios(pushRequest.getMessage(), extrasMap))
                .setOptions(Options.newBuilder().setApnsProduction(apnsProduction).setTimeToLive(86000).build())
                .build();
    }

    public boolean sendPush(PushMessage pushRequest) {
        log.info("???????????????????????????{}", gson.toJson(pushRequest));
        if (CollectionUtils.isEmpty(pushRequest.getPhoneList()) || StringUtils.isEmpty(pushRequest.getMessageId())
                || StringUtils.isEmpty(pushRequest.getTitle()) || StringUtils.isEmpty(pushRequest.getMessage())
                || pushRequest.getPlatform() == null) {
            log.error("??????????????????????????????");
        }
        boolean resultAndroid = false;
        boolean resultIOS = false;

        if ("ANDROID".equals(pushRequest.getPlatform())
                || "ALL".equals(pushRequest.getPlatform())) {
            //???Android
            log.info("???Android??????");
            PushResult pushResult = null;
            try {
                pushResult = this.sendPush(this.buildPushObject_all_alias_alert_android(pushRequest));
            } catch (Exception e) {

            }
            log.info("???Android???????????????{}", gson.toJson(pushResult));
            if (null != pushResult) {
                resultAndroid = pushResult.isResultOK();
            }
        }
        if ("IOS".equals(pushRequest.getPlatform())
                || "ALL".equals(pushRequest.getPlatform())) {
            //???IOS
            log.info("???IOS??????");
            PushResult pushResult = null;
            try {
                pushResult = this.sendPush(this.buildPushObject_all_alias_alert_ios(pushRequest));
            } catch (Exception e) {

            }
            log.info("???IOS???????????????{}", gson.toJson(pushResult));
            if (null != pushResult) {
                resultIOS = pushResult.isResultOK();
            }
        }
        log.info("??????resultAndroid: {}", resultAndroid);
        log.info("??????resultIOS: {}", resultIOS);
        return resultIOS;
    }

    public PushResult sendPush(PushPayload payload) {

        PushResult result = null;
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(jPushProperties.getMasterSecret(), jPushProperties.getAppKey(), null, clientConfig);
        try {
            log.info("???????????????????????????{}", gson.toJson(payload));
            result = jpushClient.sendPush(payload);
            log.info("???????????????????????????{}", gson.toJson(result));
            if (!result.isResultOK()) {
                log.error("?????????????????? ?????????pushRequest = {} ", gson.toJson(payload));
            }
        } catch (APIRequestException e) {
            log.error("?????????????????? APIRequestException??????###:{}", gson.toJson(e));
        } catch (APIConnectionException e) {
            log.error("?????????????????? APIConnectionException??????###:{}", e.toString());
        } catch (Exception e) {
            log.error("?????????????????? ??????###:{}", e.toString());
        }
        return result;
    }


}
