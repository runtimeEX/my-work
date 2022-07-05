package com.boss.wx.pay.channel;


import com.boss.wx.pay.model.AbstractRS;
import com.boss.wx.pay.model.request.UnifiedOrderRQ;
import com.boss.wx.pay.service.MchAppConfigContext;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.channel
 * @Description: TODO
 * @date Date : 2022年06月27日 下午5:46
 */
public interface IPaymentService {
    AbstractRS pay(MchAppConfigContext mchAppConfigContext, UnifiedOrderRQ request);
}
