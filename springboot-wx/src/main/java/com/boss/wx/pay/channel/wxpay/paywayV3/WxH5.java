package com.boss.wx.pay.channel.wxpay.paywayV3;

import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.boss.wx.message.MessageCode;
import com.boss.wx.pay.channel.wxpay.WxPayPaymentService;
import com.boss.wx.pay.channel.wxpay.kits.WxpayV3Util;
import com.boss.wx.pay.model.AbstractRS;
import com.boss.wx.pay.model.request.UnifiedOrderRQ;
import com.boss.wx.pay.model.request.WxH5OrderRQ;
import com.boss.wx.pay.model.response.WxH5OrderRS;
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
 * @date Date : 2022年06月27日 下午4:29
 */
@Slf4j
@Service("wxpayPaymentByH5V3Service")
public class WxH5 extends WxPayPaymentService {

    @Override
    public AbstractRS pay(MchAppConfigContext mchAppConfigContext, UnifiedOrderRQ request) {
        WxH5OrderRQ bizRQ = (WxH5OrderRQ) request;
        WxServiceWrapper wxServiceWrapper = mchAppConfigContext.getWxServiceWrapper();
        WxPayService wxPayService = wxServiceWrapper.getWxPayService();
        wxPayService.getConfig().setTradeType(WxPayConstants.TradeType.MWEB);

        // 构造请求数据
        JSONObject reqJSON = buildV3OrderRequest(request, mchAppConfigContext);

        JSONObject sceneInfo = reqJSON.getJSONObject("scene_info");

        JSONObject h5Info = new JSONObject();
        h5Info.put("type", "iOS, Android, Wap");
        sceneInfo.put("h5_info", h5Info);

        reqJSON.put("scene_info", sceneInfo);
        String reqUrl = WxpayV3Util.NORMALMCH_URL_MAP.get(WxPayConstants.TradeType.MWEB);

        WxH5OrderRS res = new WxH5OrderRS();
        try {
            JSONObject resJSON = WxpayV3Util.unifiedOrderV3(reqUrl, reqJSON, wxPayService);
            String payUrl = "跳转到支付页面url" + Base64.encode(resJSON.getString("h5_url"));
            res.setPayUrl(payUrl);
        } catch (WxPayException e) {
            log.error("支付异常:{}", JSONUtil.toJsonStr(reqJSON));
            MessageCode.ERROR.throwException("支付异常");
        }

        return res;
    }
}
