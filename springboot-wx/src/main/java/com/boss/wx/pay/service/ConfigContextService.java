package com.boss.wx.pay.service;

import cn.hutool.json.JSONUtil;

import com.boss.wx.pay.contants.CS;
import com.boss.wx.pay.model.NormalMchParams;
import com.boss.wx.pay.model.wxpay.WxPayNormalMchParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.service
 * @Description: TODO
 * @date Date : 2022年06月27日 上午11:35
 */
@Slf4j
@Service
public class ConfigContextService {
/*    @Autowired
    private WxPayConfigService wxPayConfigService;*/
    /**
     * <应用ID, 商户配置上下文>
     **/
    private static final Map<String, MchAppConfigContext> mchAppConfigContextMap = new ConcurrentHashMap<>();

    /**
     * 获取 [商户应用支付参数配置信息]
     **/
    public MchAppConfigContext getMchAppConfigContext(String appId, String mchId, Long shopId) {
        MchAppConfigContext mchAppConfigContext = mchAppConfigContextMap.get(appId);
        //无此数据， 需要初始化
        if (mchAppConfigContext == null || !mchAppConfigContext.getMchNo().equals(mchId)) {
            initMchAppConfigContext(appId, shopId);
        }

        return mchAppConfigContextMap.get(appId);
    }

    /**
     * 初始化 [商户应用支付参数配置信息]
     **/
    public synchronized void initMchAppConfigContext(String appId, Long shopId) {
        MchAppConfigContext mchAppConfigContext = new MchAppConfigContext();
      // WxPayNormalMchParams wxPayNormalMchParams = wxPayConfigService.payConf(shopId, appId, CS.IF_CODE.WXPAY, 1, WxPayNormalMchParams.class);
        WxPayNormalMchParams wxPayNormalMchParams=null;
        //暂时只有微信支付
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
        log.info("wxPayParams:{}", JSONUtil.toJsonStr(wxPayParams));
        if (wxPayParams != null) {
            mchAppConfigContext.setWxServiceWrapper(WxServiceWrapper.buildWxServiceWrapper(wxPayParams));
        }
        mchAppConfigContextMap.put(mchAppConfigContext.getAppId(), mchAppConfigContext);
    }
}
