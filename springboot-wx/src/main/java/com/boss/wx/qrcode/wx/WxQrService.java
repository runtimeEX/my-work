package com.boss.wx.qrcode.wx;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.boss.wx.message.MessageCode;
import com.boss.wx.pay.contants.WxRequestUrlConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-wx
 * @Package com.boss.wx.qrcode.wx
 * @Description: TODO
 * @date Date : 2022年07月05日 下午5:24
 */
@Slf4j
@Service
public class WxQrService {

    @Value("${app.wx.env_version}")
    private String envVersion;
    /**
     * 用于获取access_token
     *
     * @return access_token
     * @throws Exception
     */
    public String getAccessToken(String appId, String appSec) {
        //微信对token获取次数有限制，此token应放入缓存
        String requestUrl = MessageFormat.format(WxRequestUrlConstant.ACCESS_TOKEN, appId, appSec);
        log.info("【用于获取access_token】 requestUrl：" + requestUrl);
        HttpResponse execute = HttpRequest.post(requestUrl).execute();
        String result = execute.body();
        log.info("access_token result:{}",result);
        JSONObject jsonObject = JSON.parseObject(result);
        String token = jsonObject.getString("access_token");
        if (!StringUtils.hasText(token)) {
            MessageCode.ERROR.throwException("获取access_token失败");
        }
        log.info("【用于获取access_token】 ：" + token);
        return token;
    }

    public String getQrCode(String appId, String appSec, String scene, String page) {
        String accessToken = getAccessToken(appId, appSec);
        String encode = "";
        RestTemplate rest = new RestTemplate();
        ResponseEntity<byte[]> entity = null;
        try {
            String url = MessageFormat.format(WxRequestUrlConstant.QR_CODE, accessToken);
            log.info("调用生成微信二维码url：" + url);
            Map<String, Object> param = new HashMap<>();
            param.put("scene", scene);
            param.put("page", page);
            param.put("env_version", envVersion);
            param.put("width", 430);
            param.put("auto_color", false);
            Map<String, Object> line_color = new HashMap<>();
            line_color.put("r", 0);
            line_color.put("g", 0);
            line_color.put("b", 0);
            param.put("line_color", line_color);
            //param.put("is_hyaline", false);
            log.info("调用生成微信URL接口传参:" + param);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            HttpEntity requestEntity = new HttpEntity(param, headers);
            entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
            byte[] result = entity.getBody();
           // inputStream = new ByteArrayInputStream(result);
            encode = Base64.encode(result);
            log.info("调用小程序生成微信永久小程序码URL:{}", encode);
        } catch (Exception e) {
            log.error("调用小程序生成微信永久小程序码URL接口异常", e);
        } finally {
            HttpHeaders httpHeaders = entity.getHeaders();
            if (httpHeaders.getContentType().getSubtype().equals("json")){
                log.info("ma entity:{}",new String(entity.getBody()));
                MessageCode.ERROR.throwException("服务正忙，请稍后再试");
            }
        }
        return encode;
    }

    public static void main(String[] args) {
        new WxQrService().getQrCode("wxc6ebce156858e28f","fb185de90a198241d1191d23187e84e0","9999","pagesB/getCoupon/index");
    }

}
