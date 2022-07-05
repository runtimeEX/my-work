package com.boss.wx.pay.service;

import com.boss.wx.pay.channel.wxpay.proxy.AppWxPayProxyServiceImpl;
import com.boss.wx.pay.contants.CS;
import com.boss.wx.pay.model.wxpay.WxPayNormalMchParams;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.service
 * @Description: TODO
 * @date Date : 2022年06月27日 上午11:15
 */
@Data
@AllArgsConstructor
public class WxServiceWrapper {
    /**
     * 缓存微信API版本
     **/
    private String apiVersion;

    /**
     * 缓存 wxPayService 对象
     **/
    private WxPayService wxPayService;


    public static WxServiceWrapper buildWxServiceWrapper(String host, String password, Integer port, String username, String mchId, String appId, String appSecret, String mchKey, String apiVersion, String apiV3Key,
                                                         String serialNo, String cert, String apiClientCert, String apiClientKey) {

        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setMchId(mchId);
        wxPayConfig.setAppId(appId);
        wxPayConfig.setMchKey(mchKey);

        wxPayConfig.setHttpProxyHost(StringUtils.trimToNull(host));
        wxPayConfig.setHttpProxyPort(port);
        wxPayConfig.setHttpProxyUsername(StringUtils.trimToNull(username));
        wxPayConfig.setHttpProxyPassword(StringUtils.trimToNull(password));


        if (CS.PAY_IF_VERSION.WX_V2.equals(apiVersion)) { // 微信API  V2
            wxPayConfig.setSignType(WxPayConstants.SignType.MD5);
        }

        if (StringUtils.isNotBlank(apiV3Key)) {
            wxPayConfig.setApiV3Key(apiV3Key);
        }
        if (StringUtils.isNotBlank(serialNo)) {
            wxPayConfig.setCertSerialNo(serialNo);
        }
        if (StringUtils.isNotBlank(cert)) {
            wxPayConfig.setKeyPath(cert);
        }
        if (StringUtils.isNotBlank(apiClientCert)) {
            wxPayConfig.setPrivateCertPath(apiClientCert);
        }
        if (StringUtils.isNotBlank(apiClientKey)) {
            wxPayConfig.setPrivateKeyPath(apiClientKey);
        }

        WxPayService wxPayService = new AppWxPayProxyServiceImpl();
        wxPayService.setConfig(wxPayConfig); //微信配置信息

        return new WxServiceWrapper(apiVersion, wxPayService);
    }

    public static WxServiceWrapper buildWxServiceWrapper(WxPayNormalMchParams wxPayNormalMchParams) {
        //放置 wxJavaService
        return buildWxServiceWrapper(wxPayNormalMchParams.getHttpProxyHost(), wxPayNormalMchParams.getHttpProxyPassword(), wxPayNormalMchParams.getHttpProxyPort(), wxPayNormalMchParams.getHttpProxyUsername(), wxPayNormalMchParams.getMchId(), wxPayNormalMchParams.getAppId(),
                wxPayNormalMchParams.getAppSecret(), wxPayNormalMchParams.getKey(), wxPayNormalMchParams.getApiVersion(), wxPayNormalMchParams.getApiV3Key(),
                wxPayNormalMchParams.getSerialNo(), wxPayNormalMchParams.getCert(), wxPayNormalMchParams.getApiClientCert(), wxPayNormalMchParams.getApiClientKey());
    }

}
