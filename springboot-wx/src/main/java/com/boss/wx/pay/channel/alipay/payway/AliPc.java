package com.boss.wx.pay.channel.alipay.payway;

import com.boss.wx.pay.channel.alipay.AlipayPaymentService;
import com.boss.wx.pay.model.AbstractRS;
import com.boss.wx.pay.model.request.UnifiedOrderRQ;
import com.boss.wx.pay.service.MchAppConfigContext;
import org.springframework.stereotype.Service;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.channel.alipay.payway
 * @Description: TODO
 * @date Date : 2022年07月04日 下午3:49
 */
@Service("alipayPaymentByAliPcService")
public class AliPc extends AlipayPaymentService {

    @Override
    public AbstractRS pay(MchAppConfigContext mchAppConfigContext, UnifiedOrderRQ request) {

        return super.pay(mchAppConfigContext, request);
    }
}
