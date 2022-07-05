package com.boss.wx.share.model.response;

import lombok.Data;

@Data
public class Code2SessionResponse {

    private String openid;

    private String sessionKey;

    private String unionid;

    private Number errcode;

    private String errmsg;

}
