package com.boss.wx.pay.channel.wxpay.paywayV3;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.boss.wx.message.MessageCode;
import com.boss.wx.pay.channel.wxpay.WxPayPaymentService;
import com.boss.wx.pay.channel.wxpay.kits.WxpayV3Util;
import com.boss.wx.pay.model.AbstractRS;
import com.boss.wx.pay.model.request.UnifiedOrderRQ;
import com.boss.wx.pay.model.request.WxJsapiOrderRQ;
import com.boss.wx.pay.model.response.WxJsapiOrderRS;
import com.boss.wx.pay.service.MchAppConfigContext;
import com.boss.wx.pay.service.WxServiceWrapper;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.channel.wxpay.v3
 * @Description: TODO
 * @date Date : 2022年06月27日 下午4:07
 */
@Slf4j
@Service("wxpayPaymentByJsapiV3Service")
public class WxJsapi extends WxPayPaymentService {

    @Override
    public AbstractRS pay(MchAppConfigContext mchAppConfigContext, UnifiedOrderRQ request) {
        WxJsapiOrderRQ bizRQ = (WxJsapiOrderRQ) request;
        WxServiceWrapper wxServiceWrapper = mchAppConfigContext.getWxServiceWrapper();
        WxPayService wxPayService = wxServiceWrapper.getWxPayService();
        wxPayService.getConfig().setTradeType(WxPayConstants.TradeType.JSAPI);
        // 构造请求数据
        JSONObject reqJSON = buildV3OrderRequest(request, mchAppConfigContext);
        String reqUrl = WxpayV3Util.NORMALMCH_URL_MAP.get(WxPayConstants.TradeType.JSAPI);
        JSONObject payer = new JSONObject();

        payer.put("openid", bizRQ.getOpenid());
        reqJSON.put("payer", payer);
        WxJsapiOrderRS res = new WxJsapiOrderRS();
        try {
            JSONObject resJSON = WxpayV3Util.unifiedOrderV3(reqUrl, reqJSON, wxPayService);

            res = JSONUtil.parse(resJSON.toJSONString()).toBean(WxJsapiOrderRS.class);

            log.info("jsapi response:{}", JSONUtil.toJsonStr(res));
        } catch (WxPayException e) {
            MessageCode.ERROR.throwException("支付异常");
        }
        return res;
    }


}
