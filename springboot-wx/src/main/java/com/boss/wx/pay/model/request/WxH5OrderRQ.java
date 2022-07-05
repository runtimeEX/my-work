package com.boss.wx.pay.model.request;

import com.boss.wx.pay.contants.CS;
import lombok.Data;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.model.request
 * @Description: TODO
 * @date Date : 2022年06月27日 下午4:30
 */
@Data
public class WxH5OrderRQ extends UnifiedOrderRQ {
    /**
     * 请求参数： 支付数据包类型
     **/
    private String payDataType;

    /**
     * 构造函数
     **/
    public WxH5OrderRQ() {
        this.setWayCode(CS.PAY_WAY_CODE.WX_H5);
    }
}
