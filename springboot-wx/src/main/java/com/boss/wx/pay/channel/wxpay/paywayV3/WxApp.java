package com.boss.wx.pay.channel.wxpay.paywayV3;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.boss.wx.message.MessageCode;
import com.boss.wx.pay.channel.wxpay.WxPayPaymentService;
import com.boss.wx.pay.channel.wxpay.kits.WxpayV3Util;
import com.boss.wx.pay.model.AbstractRS;
import com.boss.wx.pay.model.request.UnifiedOrderRQ;
import com.boss.wx.pay.model.request.WxAppOrderRQ;
import com.boss.wx.pay.model.response.WxAppOrderRS;
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
 * @date Date : 2022年06月27日 下午4:15
 */
@Slf4j
@Service("wxpayPaymentByAppV3Service")
public class WxApp extends WxPayPaymentService {

    @Override
    public AbstractRS pay(MchAppConfigContext mchAppConfigContext, UnifiedOrderRQ request) {
        WxAppOrderRQ wxAppOrderRQ = (WxAppOrderRQ) request;
        WxServiceWrapper wxServiceWrapper = mchAppConfigContext.getWxServiceWrapper();
        WxPayService wxPayService = wxServiceWrapper.getWxPayService();
        wxPayService.getConfig().setTradeType(WxPayConstants.TradeType.APP);
        // 构造请求数据
        JSONObject reqJSON = buildV3OrderRequest(request, mchAppConfigContext);
        String reqUrl = WxpayV3Util.NORMALMCH_URL_MAP.get(WxPayConstants.TradeType.APP);
        WxAppOrderRS res = new WxAppOrderRS();
        try {
            JSONObject resJSON = WxpayV3Util.unifiedOrderV3(reqUrl, reqJSON, wxPayService);
            res = JSONUtil.parse(resJSON.toJSONString()).toBean(WxAppOrderRS.class);
            log.info("wxapp response:{}", JSONUtil.toJsonStr(res));
        } catch (WxPayException e) {
            MessageCode.ERROR.throwException("支付异常");
        }
        return res;
    }
}
