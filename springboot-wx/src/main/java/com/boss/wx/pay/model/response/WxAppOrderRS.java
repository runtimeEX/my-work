package com.boss.wx.pay.model.response;

import com.boss.wx.pay.model.AbstractRS;
import lombok.Data;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.model.response
 * @Description: TODO
 * @date Date : 2022年06月27日 下午4:21
 */
@Data
public class WxAppOrderRS extends AbstractRS {

    private String appId;
    private String partnerId;
    private String prepayId;
    private String packageValue;
    private String nonceStr;
    private String timestamp;
    private String sign;
}
