/*
package com.boss.wx.pay.service;

import cn.vpclub.ghs.bff.pay.contants.CS;
import cn.vpclub.ghs.bff.pay.model.NormalMchParams;
import cn.vpclub.ghs.bff.pay.model.wxpay.WxPayNormalMchParams;
import cn.vpclub.ghs.bff.wxmini.login.constant.WxRequestUrlConstant;
import cn.vpclub.ghs.models.pay.conf.entity.PayConf;
import cn.vpclub.ghs.models.pay.conf.service.PayConfDomainService;
import cn.vpclub.ghs.sdk.api.ghp.shop.info.ShopClient;
import cn.vpclub.ghs.sdk.api.ghp.shop.info.model.response.ShopQueryClientResponse;
import cn.vpclub.sd.framework.web.message.MessageCode;
import cn.vpclub.sd.framework.wx.pay.spring.boot.properties.AppWxPayProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

*/
/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.service
 * @Description: TODO
 * @date Date : 2022年06月27日 下午4:53
 *//*

@Slf4j
@Service
public class WxPayConfigService {

    @Autowired
    private PayConfDomainService payConfDomainService;

    @Resource
    private AppWxPayProperties appWxPayProperties;

    @Autowired
    private ShopClient shopClient;


    public PayConf query(int payType, String appId, Long shopId) {
        ShopQueryClientResponse shop = shopClient.queryById(shopId).getDataInfo();
        LambdaQueryWrapper<PayConf> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayConf::getAppId, appId);
        queryWrapper.eq(PayConf::getPayType, payType);
        queryWrapper.eq(PayConf::getEnableUse, 1);
        //总店
        if (shop.getShopRoleId() == 1) {
            queryWrapper.eq(PayConf::getShopId, shopId);
        } else {
            if (StringUtils.isEmpty(shop.getPayModel())) {
                MessageCode.ERROR.throwException("请检查店铺支付配置");
            }
            //跟随总店
            if (0 == shop.getPayModel()) {
                //总店id
                Long zId = shopClient.mainStore(shopId).getDataInfo();
                queryWrapper.eq(PayConf::getShopId, zId);
            }
            if (1 == shop.getPayModel()) {
                queryWrapper.eq(PayConf::getShopId, shopId);
            }
        }
        List<PayConf> confList = payConfDomainService.list(queryWrapper);
        if (CollectionUtils.isEmpty(confList)) {
            MessageCode.ERROR.throwException("请检查小程序配置");
        }
        return confList.get(0);
    }

    public String getOpenId(String appId, String appSec, String code) {
        String url = MessageFormat.format(WxRequestUrlConstant.XCX_GET_OPENID, appId, appSec, code);
        log.info("url: {}", url);
        RestTemplate rest = new RestTemplate();
        String sessionText = rest.getForObject(url, String.class);
        log.info("sessionText: {}", sessionText);
        JSONObject jsonObject = JSON.parseObject(sessionText);
        String openid = jsonObject.getString("openid");
        if (StringUtils.isEmpty(openid)) {
            MessageCode.ERROR.throwException("获取openid失败");
        }
        return openid;
    }

    public <T> T payConf(Long shopId, String appId, String ifCode, int payType, Class<? extends NormalMchParams> cls) {
        PayConf payConf = query(payType, appId, shopId);
        if (CS.IF_CODE.WXPAY.equals(ifCode)) {
            WxPayNormalMchParams params = new WxPayNormalMchParams();
            params.setApiVersion(CS.PAY_IF_VERSION.WX_V3);
            params.setAppId(payConf.getAppId());
            params.setAppSecret(payConf.getAppSecret());
            params.setApiClientCert(payConf.getPrivateCertPath());
            params.setApiClientKey(payConf.getPrivateKeyPath());
            params.setApiV3Key(payConf.getApiV3Key());
            params.setCert(payConf.getKeyPath());
            params.setMchId(payConf.getMchId());
            params.setSerialNo(payConf.getCertSerialNo());
            params.setHttpProxyHost(this.appWxPayProperties.getHttpProxyHost());
            params.setHttpProxyPort(this.appWxPayProperties.getHttpProxyPort());
            params.setHttpProxyUsername(this.appWxPayProperties.getHttpProxyUsername());
            params.setHttpProxyPassword(this.appWxPayProperties.getHttpProxyPassword());
            return (T) params;
        }
        return null;
    }
}
*/
