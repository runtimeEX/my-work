package com.boss.wx.pay.channel.wxpay;

import com.alibaba.fastjson.JSONObject;
import com.boss.wx.message.MessageCode;
import com.boss.wx.pay.channel.IPaymentService;
import com.boss.wx.pay.contants.CS;
import com.boss.wx.pay.model.AbstractRS;
import com.boss.wx.pay.model.request.UnifiedOrderRQ;
import com.boss.wx.pay.service.MchAppConfigContext;
import com.boss.wx.pay.service.WxServiceWrapper;
import com.boss.wx.pay.utils.PaywayUtil;
import org.springframework.stereotype.Service;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.channel.wxpay
 * @Description: TODO
 * @date Date : 2022年06月27日 下午2:32
 */
@Service("wxPayPaymentService")
public class WxPayPaymentService implements IPaymentService {

    public JSONObject buildV3OrderRequest(UnifiedOrderRQ payOrder, MchAppConfigContext mchAppConfigContext) {
        // 微信统一下单请求对象
        JSONObject reqJSON = new JSONObject();
        reqJSON.put("out_trade_no", payOrder.getOrderNo());
        reqJSON.put("description", payOrder.getDescription());
        // 订单失效时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE,示例值：2018-06-08T10:34:56+08:00
        //   reqJSON.put("time_expire", String.format("%sT%s+08:00", DateUtil.format(payOrder.getExpiredTime(), DatePattern.NORM_DATE_FORMAT), DateUtil.format(payOrder.getExpiredTime(), DatePattern.NORM_TIME_FORMAT)));

        reqJSON.put("notify_url", payOrder.getNotifyUrl());

        JSONObject amount = new JSONObject();
        amount.put("total", payOrder.getOrderActualPrice());
        amount.put("currency", "CNY");
        reqJSON.put("amount", amount);

        JSONObject sceneInfo = new JSONObject();
        sceneInfo.put("payer_client_ip", payOrder.getIp());
        reqJSON.put("scene_info", sceneInfo);

        reqJSON.put("appid", mchAppConfigContext.getAppId());
        reqJSON.put("mchid", mchAppConfigContext.getMchNo());

        return reqJSON;
    }


    @Override
    public AbstractRS pay(MchAppConfigContext mchAppConfigContext, UnifiedOrderRQ request) {
        WxServiceWrapper wxServiceWrapper = mchAppConfigContext.getWxServiceWrapper();
        String apiVersion = wxServiceWrapper.getApiVersion();
        if (CS.PAY_IF_VERSION.WX_V2.equals(apiVersion)) {
            return PaywayUtil.getRealPaywayService(this, request.getWayCode()).pay(mchAppConfigContext, request);
        } else if (CS.PAY_IF_VERSION.WX_V3.equals(apiVersion)) {
            return PaywayUtil.getRealPaywayV3Service(this, request.getWayCode()).pay(mchAppConfigContext, request);
        } else {
            MessageCode.ERROR.throwException("不支持的微信支付API版本");
        }
        return null;
    }
}
