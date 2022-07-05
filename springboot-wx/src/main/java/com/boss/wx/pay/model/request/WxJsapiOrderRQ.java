package com.boss.wx.pay.model.request;

import com.boss.wx.pay.contants.CS;
import lombok.Data;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.model.request
 * @Description: TODO
 * @date Date : 2022年06月27日 下午2:37
 */
@Data
public class WxJsapiOrderRQ extends UnifiedOrderRQ {
    /**
     * 微信openid
     **/
    private String openid;

    /**
     * 构造函数
     **/
    public WxJsapiOrderRQ() {
        this.setWayCode(CS.PAY_WAY_CODE.WX_JSAPI);
    }
}
