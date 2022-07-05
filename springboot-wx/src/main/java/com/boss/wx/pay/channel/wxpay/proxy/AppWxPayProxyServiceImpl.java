package com.boss.wx.pay.channel.wxpay.proxy;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.v3.WxPayV3HttpClientBuilder;
import com.github.binarywang.wxpay.v3.auth.PrivateKeySigner;
import com.github.binarywang.wxpay.v3.auth.WxPayCredentials;
import com.github.binarywang.wxpay.v3.auth.WxPayValidator;
import com.github.binarywang.wxpay.v3.util.PemUtils;
import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jodd.util.ResourcesUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.util.json.GsonParser;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Collections;

/**
 * postV3代理
 */
@Slf4j
public class AppWxPayProxyServiceImpl extends WxPayServiceImpl {

    private static final String PROBLEM_MSG = "证书文件【%s】有问题，请核实！";
    private static final String NOT_FOUND_MSG = "证书文件【%s】不存在，请核实！";

    public AppWxPayProxyServiceImpl() {
        log.info("初始化微信支付，使用代理 {}", this.getClass().getSimpleName());
    }


    /**
     * 从配置路径 加载配置 信息（支持 classpath、本地路径、网络url）
     * @param configPath 配置路径
     * @return
     * @throws WxPayException
     */
    private InputStream loadConfigInputStream(String configPath) throws WxPayException {
        InputStream inputStream;
        final String prefix = "classpath:";
        String fileHasProblemMsg = String.format(PROBLEM_MSG, configPath);
        String fileNotFoundMsg = String.format(NOT_FOUND_MSG, configPath);
        if (configPath.startsWith(prefix)) {
            String path = RegExUtils.removeFirst(configPath, prefix);
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            try {
                inputStream = ResourcesUtil.getResourceAsStream(path);
                if (inputStream == null) {
                    throw new WxPayException(fileNotFoundMsg);
                }
            } catch (Exception e) {
                throw new WxPayException(fileNotFoundMsg, e);
            }
        } else if (configPath.startsWith("http://") || configPath.startsWith("https://")) {
            try {
                inputStream = new URL(configPath).openStream();
                if (inputStream == null) {
                    throw new WxPayException(fileNotFoundMsg);
                }
            } catch (IOException e) {
                throw new WxPayException(fileNotFoundMsg, e);
            }
        } else {
            try {
                File file = new File(configPath);
                if (!file.exists()) {
                    throw new WxPayException(fileNotFoundMsg);
                }

                inputStream = new FileInputStream(file);
            } catch (IOException e) {
                throw new WxPayException(fileHasProblemMsg, e);
            }
        }
        return inputStream;
    }

    private CloseableHttpClient createApiV3HttpClient() throws WxPayException {
        CloseableHttpClient apiV3HttpClient = this.getConfig().getApiV3HttpClient();
        if (null == apiV3HttpClient) {
            apiV3HttpClient = this.initApiV3HttpClient();
            this.getConfig().setApiV3HttpClient(apiV3HttpClient);
        }
        return apiV3HttpClient;
    }

    public CloseableHttpClient initApiV3HttpClient() throws WxPayException {
        WxPayConfig wxPayConfig = this.getConfig();
        String privateKeyPath = wxPayConfig.getPrivateKeyPath();
        String privateCertPath = wxPayConfig.getPrivateCertPath();
        String serialNo = wxPayConfig.getCertSerialNo();
        String apiV3Key = wxPayConfig.getApiV3Key();
        if (StringUtils.isBlank(privateKeyPath)) {
            throw new WxPayException("请确保privateKeyPath已设置");
        }
        if (StringUtils.isBlank(privateCertPath)) {
            throw new WxPayException("请确保privateCertPath已设置");
        }
//    if (StringUtils.isBlank(certSerialNo)) {
//      throw new WxPayException("请确保certSerialNo证书序列号已设置");
//    }
        if (StringUtils.isBlank(apiV3Key)) {
            throw new WxPayException("请确保apiV3Key值已设置");
        }

        InputStream keyInputStream = this.loadConfigInputStream(privateKeyPath);
        InputStream certInputStream = this.loadConfigInputStream(privateCertPath);
        try {
            PrivateKey merchantPrivateKey = PemUtils.loadPrivateKey(keyInputStream);
            X509Certificate certificate = PemUtils.loadCertificate(certInputStream);
            if(StringUtils.isBlank(serialNo)){
                serialNo = certificate.getSerialNumber().toString(16).toUpperCase();
                wxPayConfig.setCertSerialNo(serialNo);
            }

            AutoUpdateCertificatesProxyVerifier verifier = new AutoUpdateCertificatesProxyVerifier(this.getConfig().getHttpProxyHost(), this.getConfig().getHttpProxyPort(),
                    new WxPayCredentials(wxPayConfig.getMchId(), new PrivateKeySigner(serialNo, merchantPrivateKey)),
                    apiV3Key.getBytes(StandardCharsets.UTF_8), wxPayConfig.getCertAutoUpdateTime());

            WxPayV3HttpClientBuilder wxPayV3HttpClientBuilder = WxPayV3HttpClientBuilder.create();

            if (!Strings.isNullOrEmpty(this.getConfig().getHttpProxyHost()) && this.getConfig().getHttpProxyPort() != null && this.getConfig().getHttpProxyPort() > 0) {
                log.info("微信支付使用代理 {} {}",this.getConfig().getHttpProxyHost(), this.getConfig().getHttpProxyPort());
                wxPayV3HttpClientBuilder.setProxy(new HttpHost(this.getConfig().getHttpProxyHost(), this.getConfig().getHttpProxyPort()));
            }else {
                log.info("微信支付未使用代理");
            }

            CloseableHttpClient httpClient = wxPayV3HttpClientBuilder
                    .withMerchant(wxPayConfig.getMchId(), serialNo, merchantPrivateKey)
                    .withWechatpay(Collections.singletonList(certificate))
                    .withValidator(new WxPayValidator(verifier))
                    .build();
            wxPayConfig.setApiV3HttpClient(httpClient);
            wxPayConfig.setVerifier(verifier);
            wxPayConfig.setPrivateKey(merchantPrivateKey);
            return httpClient;
        } catch (Exception e) {
            throw new WxPayException("v3请求构造异常！", e);
        }
    }


    private HttpPost createHttpPost(String url, String requestStr) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(this.createEntry(requestStr));

        RequestConfig.Builder builder = RequestConfig.custom();

        httpPost.setConfig(builder.setConnectionRequestTimeout(this.getConfig().getHttpConnectionTimeout())
                .setConnectTimeout(this.getConfig().getHttpConnectionTimeout())
                .setSocketTimeout(this.getConfig().getHttpTimeout())
                .build());

        return httpPost;
    }

    private StringEntity createEntry(String requestStr) {
        return new StringEntity(requestStr, ContentType.create("application/json", "utf-8"));
        //return new StringEntity(new String(requestStr.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
    }

    @Override
    public String postV3(String url, String requestStr) throws WxPayException {
        CloseableHttpClient httpClient = this.createApiV3HttpClient();
        HttpPost httpPost = this.createHttpPost(url, requestStr);
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-Type", "application/json");
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            //v3已经改为通过状态码判断200 204 成功
            int statusCode = response.getStatusLine().getStatusCode();
            //post方法有可能会没有返回值的情况
            String responseString;
            if (response.getEntity() == null) {
                responseString = null;
            } else {
                responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
            if (HttpStatus.SC_OK == statusCode || HttpStatus.SC_NO_CONTENT == statusCode) {
                log.info("\n【请求地址】：{}\n【请求数据】：{}\n【响应数据】：{}", url, requestStr, responseString);
                return responseString;
            } else {
                //有错误提示信息返回
                JsonObject jsonObject = GsonParser.parse(responseString);
                throw convertException(jsonObject);
            }
        } catch (Exception e) {
            log.error("\n【请求地址】：{}\n【请求数据】：{}\n【异常信息】：{}", url, requestStr, e.getMessage());
            throw (e instanceof WxPayException) ? (WxPayException) e : new WxPayException(e.getMessage(), e);
        } finally {
            httpPost.releaseConnection();
        }


    }

    private WxPayException convertException(JsonObject jsonObject) {
        //todo 这里考虑使用新的适用于V3的异常
        JsonElement codeElement = jsonObject.get("code");
        String code = codeElement == null ? null : codeElement.getAsString();
        String message = jsonObject.get("message").getAsString();
        WxPayException wxPayException = new WxPayException(message);
        wxPayException.setErrCode(code);
        wxPayException.setErrCodeDes(message);
        return wxPayException;
    }


}
