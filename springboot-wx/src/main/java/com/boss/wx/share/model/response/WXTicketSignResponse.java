package com.boss.wx.share.model.response;

import lombok.Data;

@Data
public class WXTicketSignResponse {
    private String timestamp;
    private String nonceStr;
    private String signature;
    //业务id，可有可无
    private Long shareManId;
    private String appId;
}