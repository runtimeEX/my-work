package com.boss.wx.model.response;

import com.boss.wx.model.AbstractRS;
import lombok.Data;

@Data
public class WxJsapiOrderRS extends AbstractRS {

    /**
     * 预支付数据包
     **/
    private String payInfo;
}
