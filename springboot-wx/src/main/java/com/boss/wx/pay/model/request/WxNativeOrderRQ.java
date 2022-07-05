package com.boss.wx.pay.model.request;

import com.boss.wx.pay.contants.CS;
import lombok.Data;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.model.request
 * @Description: TODO
 * @date Date : 2022年06月30日 下午5:37
 */
@Data
public class WxNativeOrderRQ extends UnifiedOrderRQ {
    /**
     * 构造函数
     **/
    public WxNativeOrderRQ() {
        this.setWayCode(CS.PAY_WAY_CODE.WX_NATIVE);
    }
}
