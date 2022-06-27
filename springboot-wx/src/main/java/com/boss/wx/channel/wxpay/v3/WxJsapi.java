package com.boss.wx.channel.wxpay.v3;

import com.alibaba.fastjson.JSONObject;
import com.boss.wx.channel.wxpay.WxPayPaymentService;
import com.boss.wx.channel.wxpay.kits.WxpayV3Util;
import com.boss.wx.constants.CS;
import com.boss.wx.model.AbstractRS;
import com.boss.wx.model.MchAppConfigContext;
import com.boss.wx.model.NormalMchParams;
import com.boss.wx.model.WxServiceWrapper;
import com.boss.wx.model.response.WxJsapiOrderRS;
import com.boss.wx.service.ConfigContextService;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信 jsapi支付
 */
@Slf4j
@Service
public class WxJsapi extends WxPayPaymentService {
    @Autowired
    private ConfigContextService configContextService;

    public AbstractRS pay(NormalMchParams normalMchParams, String code, Object payOrder) throws Exception {
        MchAppConfigContext mchAppConfigContext = configContextService.getMchAppConfigContext(normalMchParams, CS.IF_CODE.WXPAY);
        WxServiceWrapper wxServiceWrapper = mchAppConfigContext.getWxServiceWrapper();
        WxPayService wxPayService = wxServiceWrapper.getWxPayService();
        wxPayService.getConfig().setTradeType(WxPayConstants.TradeType.JSAPI);
        // 构造请求数据
        JSONObject reqJSON = buildV3OrderRequest(payOrder, mchAppConfigContext);
        String reqUrl = WxpayV3Util.NORMALMCH_URL_MAP.get(WxPayConstants.TradeType.JSAPI);
        JSONObject payer = new JSONObject();

        payer.put("openid", wxServiceWrapper.getWxMpService().getOAuth2Service().getAccessToken(code).getOpenId());
        reqJSON.put("payer", payer);

        JSONObject resJSON = WxpayV3Util.unifiedOrderV3(reqUrl, reqJSON, wxPayService);
        WxJsapiOrderRS res = new WxJsapiOrderRS();
        res.setPayInfo(resJSON.toJSONString());
        return res;
    }
}
