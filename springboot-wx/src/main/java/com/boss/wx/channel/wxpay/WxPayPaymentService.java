package com.boss.wx.channel.wxpay;

import com.alibaba.fastjson.JSONObject;
import com.boss.wx.model.MchAppConfigContext;


public class WxPayPaymentService {
    /**
     * 构建微信APIV3接口  统一下单请求数据
     *
     * @param payOrder
     * @return
     */
    public JSONObject buildV3OrderRequest(Object payOrder, MchAppConfigContext mchAppConfigContext) {
        String payOrderId = "";

        // 微信统一下单请求对象
        JSONObject reqJSON = new JSONObject();
        reqJSON.put("out_trade_no", payOrderId);
        reqJSON.put("description", "");
        // 订单失效时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE,示例值：2018-06-08T10:34:56+08:00
        //   reqJSON.put("time_expire", String.format("%sT%s+08:00", DateUtil.format(payOrder.getExpiredTime(), DatePattern.NORM_DATE_FORMAT), DateUtil.format(payOrder.getExpiredTime(), DatePattern.NORM_TIME_FORMAT)));

        reqJSON.put("notify_url", "");

        JSONObject amount = new JSONObject();
        amount.put("total", 666);
        amount.put("currency", "CNY");
        reqJSON.put("amount", amount);

        JSONObject sceneInfo = new JSONObject();
        sceneInfo.put("payer_client_ip", "");
        reqJSON.put("scene_info", sceneInfo);

        reqJSON.put("appid", mchAppConfigContext.getAppId());
        reqJSON.put("mchid", mchAppConfigContext.getMchNo());

        return reqJSON;
    }

}
