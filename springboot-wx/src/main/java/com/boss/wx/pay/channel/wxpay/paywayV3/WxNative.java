package com.boss.wx.pay.channel.wxpay.paywayV3;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.boss.wx.message.MessageCode;
import com.boss.wx.pay.channel.wxpay.WxPayPaymentService;
import com.boss.wx.pay.channel.wxpay.kits.WxpayV3Util;
import com.boss.wx.pay.model.AbstractRS;
import com.boss.wx.pay.model.request.UnifiedOrderRQ;
import com.boss.wx.pay.model.request.WxNativeOrderRQ;
import com.boss.wx.pay.model.response.WxNativeOrderRS;
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
 * @Package cn.vpclub.ghs.bff.pay.channel.wxpay.paywayV3
 * @Description: TODO
 * @date Date : 2022年06月30日 下午5:40
 */
@Slf4j
@Service("wxpayPaymentByNativeV3Service")
public class WxNative extends WxPayPaymentService {

    @Override
    public AbstractRS pay(MchAppConfigContext mchAppConfigContext, UnifiedOrderRQ request) {
        WxNativeOrderRQ bizRQ = (WxNativeOrderRQ) request;
        WxServiceWrapper wxServiceWrapper = mchAppConfigContext.getWxServiceWrapper();
        WxPayService wxPayService = wxServiceWrapper.getWxPayService();
        wxPayService.getConfig().setTradeType(WxPayConstants.TradeType.NATIVE);
        // 构造请求数据
        JSONObject reqJSON = buildV3OrderRequest(request, mchAppConfigContext);
        String reqUrl = WxpayV3Util.NORMALMCH_URL_MAP.get(WxPayConstants.TradeType.NATIVE);

        WxNativeOrderRS res = new WxNativeOrderRS();
        try {
            JSONObject resJSON = WxpayV3Util.unifiedOrderV3(reqUrl, reqJSON, wxPayService);
            String codeUrl = resJSON.getString("code_url");
            res.setCodeUrl(codeUrl);
            log.info("native response:{}", JSONUtil.toJsonStr(res));
        } catch (WxPayException e) {
            MessageCode.ERROR.throwException("支付异常");
        }
        return res;
    }

}
