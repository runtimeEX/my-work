package com.boss.wx.channel.wxpay.v3;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import com.boss.wx.channel.wxpay.WxPayPaymentService;
import com.boss.wx.channel.wxpay.kits.WxpayV3Util;
import com.boss.wx.model.AbstractRS;
import com.boss.wx.model.MchAppConfigContext;
import com.boss.wx.model.NormalMchParams;
import com.boss.wx.model.WxServiceWrapper;
import com.boss.wx.model.response.WxH5OrderRS;
import com.boss.wx.service.ConfigContextService;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信 app支付
 */
@Slf4j
@Service
public class WxH5 extends WxPayPaymentService {
    @Autowired
    private ConfigContextService configContextService;

    public AbstractRS pay(NormalMchParams normalMchParams, String code, Object payOrder) throws Exception {
        MchAppConfigContext mchAppConfigContext = configContextService.getMchAppConfigContext(normalMchParams, code);
        WxServiceWrapper wxServiceWrapper = mchAppConfigContext.getWxServiceWrapper();
        WxPayService wxPayService = wxServiceWrapper.getWxPayService();
        wxPayService.getConfig().setTradeType(WxPayConstants.TradeType.MWEB);
        // 构造请求数据
        JSONObject reqJSON = buildV3OrderRequest(payOrder, mchAppConfigContext);
        String reqUrl = WxpayV3Util.NORMALMCH_URL_MAP.get(WxPayConstants.TradeType.MWEB);
        JSONObject resJSON = WxpayV3Util.unifiedOrderV3(reqUrl, reqJSON, wxPayService);
        String payUrl = "网关url" + Base64.encode(resJSON.getString("h5_url"));
        WxH5OrderRS res = new WxH5OrderRS();
        res.setPayUrl(payUrl);
        return res;
    }
}
