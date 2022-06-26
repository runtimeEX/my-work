package com.boss.wx.service;

import cn.hutool.json.JSONUtil;
import com.boss.wx.constants.CS;
import com.boss.wx.model.MchAppConfigContext;
import com.boss.wx.model.NormalMchParams;
import com.boss.wx.model.WxServiceWrapper;
import com.boss.wx.model.wxpay.WxPayNormalMchParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-wx
 * @Package com.boss.wx.service
 * @Description: 配置信息上下文服务
 * @date Date : 2022年06月23日 下午5:38
 */
@Slf4j
@Service
public class ConfigContextService {

    /**
     * <应用ID, 商户配置上下文>
     **/
    private static final Map<String, MchAppConfigContext> mchAppConfigContextMap = new ConcurrentHashMap<>();

    /**
     * 获取 [商户应用支付参数配置信息]
     **/
    public MchAppConfigContext getMchAppConfigContext(NormalMchParams normalMchParams, String code) {
        if (CS.IF_CODE.WXPAY.equals(code)) {
            WxPayNormalMchParams wxPayNormalMchParams = (WxPayNormalMchParams) normalMchParams;
            MchAppConfigContext mchAppConfigContext = mchAppConfigContextMap.get(wxPayNormalMchParams.getAppId());
            //无此数据， 需要初始化
            /*if (mchAppConfigContext == null) {

            }*/
            initMchAppConfigContext(wxPayNormalMchParams, code);

            return mchAppConfigContextMap.get(wxPayNormalMchParams.getAppId());
        }
        return null;
    }

    /**
     * 初始化 [商户应用支付参数配置信息]
     **/
    public synchronized void initMchAppConfigContext(NormalMchParams normalMchParams, String code) {
        MchAppConfigContext mchAppConfigContext = new MchAppConfigContext();
        //暂时只有微信支付
        if (CS.IF_CODE.WXPAY.equals(code)) {
            WxPayNormalMchParams wxPayNormalMchParams = (WxPayNormalMchParams) normalMchParams;
            // 设置商户信息
            mchAppConfigContext.setAppId(wxPayNormalMchParams.getAppId());
            mchAppConfigContext.setMchNo(wxPayNormalMchParams.getMchId());
            //  mchAppConfigContext.setMchType(mchInfo.getType());

            mchAppConfigContext.getNormalMchParamsMap().put(
                    CS.IF_CODE.WXPAY,
                    NormalMchParams.factory(CS.IF_CODE.WXPAY, JSONUtil.toJsonStr(wxPayNormalMchParams))
            );
            //放置 wxJavaService
            WxPayNormalMchParams wxPayParams = mchAppConfigContext.getNormalMchParamsByIfCode(CS.IF_CODE.WXPAY, WxPayNormalMchParams.class);
            if (wxPayParams != null) {
                mchAppConfigContext.setWxServiceWrapper(WxServiceWrapper.buildWxServiceWrapper(wxPayParams));
            }
            mchAppConfigContextMap.put(mchAppConfigContext.getAppId(), mchAppConfigContext);
        }
    }
}
