package com.boss.wx.pay.channel.alipay;


import com.boss.wx.pay.channel.IPaymentService;
import com.boss.wx.pay.model.AbstractRS;
import com.boss.wx.pay.model.request.UnifiedOrderRQ;
import com.boss.wx.pay.service.MchAppConfigContext;
import com.boss.wx.pay.utils.PaywayUtil;
import org.springframework.stereotype.Service;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Description: TODO
 * @date Date : 2022年07月04日 上午11:12
 */
@Service("alipayPaymentService")
public class AlipayPaymentService implements IPaymentService {
    @Override
    public AbstractRS pay(MchAppConfigContext mchAppConfigContext, UnifiedOrderRQ request) {
        return PaywayUtil.getRealPaywayService(this, request.getWayCode()).pay(mchAppConfigContext, request);
    }
}
