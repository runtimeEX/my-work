package com.boss.wx.model;

import com.alibaba.fastjson.JSONObject;
import com.boss.wx.constants.CS;
import com.boss.wx.model.wxpay.WxPayNormalMchParams;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-wx
 * @Package com.boss.wx.model.wxpay
 * @Description: TODO
 * @date Date : 2022年06月23日 下午6:23
 */
public abstract class NormalMchParams {
    public static NormalMchParams factory(String ifCode, String paramsStr){

        if(CS.IF_CODE.WXPAY.equals(ifCode)){
            return JSONObject.parseObject(paramsStr, WxPayNormalMchParams.class);
        }/*else if(CS.IF_CODE.ALIPAY.equals(ifCode)){
            return JSONObject.parseObject(paramsStr, AlipayNormalMchParams.class);
        }else if(CS.IF_CODE.XXPAY.equals(ifCode)){
            return JSONObject.parseObject(paramsStr, XxpayNormalMchParams.class);
        }else if (CS.IF_CODE.PPPAY.equals(ifCode)){
            return JSONObject.parseObject(paramsStr, PpPayNormalMchParams.class);
        }*/
        return null;
    }

    /**
     *  敏感数据脱敏
     */
    public abstract String deSenData();
}
