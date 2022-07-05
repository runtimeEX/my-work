package com.boss.wx.pay.model;

import com.alibaba.fastjson.JSONObject;
import com.boss.wx.pay.contants.CS;
import com.boss.wx.pay.model.wxpay.WxPayNormalMchParams;
import lombok.Data;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.model
 * @Description: TODO
 * @date Date : 2022年06月27日 上午11:09
 */
@Data
public abstract class NormalMchParams {
    public static NormalMchParams factory(String ifCode, String paramsStr) {

        if (CS.IF_CODE.WXPAY.equals(ifCode)) {
            return JSONObject.parseObject(paramsStr, WxPayNormalMchParams.class);
        }
        return null;
    }

    private String httpProxyHost;
    private Integer httpProxyPort;
    private String httpProxyUsername;
    private String httpProxyPassword;

    /**
     * 敏感数据脱敏
     */
    public abstract String deSenData();
}
