package com.boss.wx.pay.model.response;

import com.boss.wx.pay.model.AbstractRS;
import lombok.Data;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.model.response
 * @Description: TODO
 * @date Date : 2022年06月27日 下午2:33
 */
@Data
public class WxJsapiOrderRS extends AbstractRS {
    private String appId;
    private String timeStamp;
    private String nonceStr;
    private String packageValue;
    private String signType;
    private String paySign;

}
