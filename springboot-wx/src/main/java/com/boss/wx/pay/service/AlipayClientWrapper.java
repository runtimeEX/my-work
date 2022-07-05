package com.boss.wx.pay.service;

import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.boss.wx.pay.contants.CS;
import com.boss.wx.pay.model.alipay.AlipayNormalMchParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.service
 * @Description: TODO
 * @date Date : 2022年07月04日 下午4:20
 */
@Data
@Slf4j
@AllArgsConstructor
public class AlipayClientWrapper {

    //默认为 不使用证书方式
    private Integer useCert = CS.NO;
    /**
     * 缓存支付宝client 对象
     **/
    private AlipayClient alipayClient;


    /*
     * 构建支付宝client 包装类
     *
     * @author terrfly
     * @site https://www.jeequan.com
     * @date 2021/6/8 17:46
     */
    public static AlipayClientWrapper buildAlipayClientWrapper(String host, Integer port, Integer useCert, Integer sandbox, String appId, String privateKey, String alipayPublicKey, String signType, String appCert,
                                                               String alipayPublicCert, String alipayRootCert) {

        //避免空值
        sandbox = sandbox == null ? CS.NO : sandbox;

        AlipayClient alipayClient = null;
        if (useCert != null && useCert == CS.YES) { //证书的方式
            CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
            certAlipayRequest.setServerUrl(sandbox == CS.YES ? CS.SANDBOX_SERVER_URL : CS.PROD_SERVER_URL);
            certAlipayRequest.setAppId(appId);
            certAlipayRequest.setPrivateKey(privateKey);
            certAlipayRequest.setFormat(CS.FORMAT);
            certAlipayRequest.setCharset(CS.CHARSET);
            certAlipayRequest.setSignType(signType);
            if (StringUtils.hasText(host)) {
                certAlipayRequest.setProxyHost(host);
                certAlipayRequest.setProxyPort(port);
                log.info("支付宝支付使用代理");
            }

            certAlipayRequest.setCertPath(appCert);
            certAlipayRequest.setAlipayPublicCertPath(alipayPublicCert);
            certAlipayRequest.setRootCertPath(alipayRootCert);
            try {
                alipayClient = new DefaultAlipayClient(certAlipayRequest);
            } catch (AlipayApiException e) {
                log.error("error", e);
                alipayClient = null;
            }
        } else {
            if (StringUtils.hasText(host)) {
                alipayClient = new DefaultAlipayClient(sandbox == CS.YES ? CS.SANDBOX_SERVER_URL : CS.PROD_SERVER_URL
                        , appId, privateKey, CS.FORMAT, CS.CHARSET,
                        alipayPublicKey, signType, host, port);
                log.info("支付宝支付使用代理");
            } else {
                alipayClient = new DefaultAlipayClient(sandbox == CS.YES ? CS.SANDBOX_SERVER_URL : CS.PROD_SERVER_URL
                        , appId, privateKey, CS.FORMAT, CS.CHARSET,
                        alipayPublicKey, signType);
            }

        }

        return new AlipayClientWrapper(useCert, alipayClient);
    }


    public static AlipayClientWrapper buildAlipayClientWrapper(AlipayNormalMchParams alipayParams) {
        log.info("alipayParams:{}", JSONUtil.toJsonStr(alipayParams));
        return buildAlipayClientWrapper(alipayParams.getHttpProxyHost(), alipayParams.getHttpProxyPort(),
                alipayParams.getUseCert(), alipayParams.getSandbox(), alipayParams.getAppId(), alipayParams.getPrivateKey(),
                alipayParams.getAlipayPublicKey(), alipayParams.getSignType(), alipayParams.getAppPublicCert(),
                alipayParams.getAlipayPublicCert(), alipayParams.getAlipayRootCert()
        );
    }


}
