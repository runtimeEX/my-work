package com.boss.wx.pay.model.request;

import com.boss.wx.pay.contants.CS;
import lombok.Data;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.model.request
 * @Description: TODO
 * @date Date : 2022年07月04日 上午11:19
 */
@Data
public class AliJsapiOrderRQ extends UnifiedOrderRQ{
    /** 构造函数 **/
    public AliJsapiOrderRQ(){
        this.setWayCode(CS.PAY_WAY_CODE.ALI_JSAPI);
    }
}
