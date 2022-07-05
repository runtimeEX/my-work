package com.boss.wx.share.gzh;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.boss.wx.message.MessageCode;
import com.boss.wx.pay.contants.WxRequestUrlConstant;
import com.boss.wx.qrcode.wx.WxQrService;
import com.boss.wx.share.model.response.Code2SessionResponse;
import com.boss.wx.share.model.response.WXTicketSignResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-wx
 * @Package com.boss.wx.share.gzh
 * @Description: TODO
 * @date Date : 2022年07月05日 下午6:11
 */
@Slf4j
@Service
public class GzhShareService {
    @Autowired
    private WxQrService wxQrService;

    @Autowired
    private ObjectMapper mapper;

    //获取分享公众号ticket，此ticket要放入缓存
    public String getTicket(String accessToken, String appId) {
        String requestUrl = MessageFormat.format(WxRequestUrlConstant.GZH_TICKET, accessToken);
        HttpResponse execute = HttpRequest.get(requestUrl).execute();
        String result = execute.body();
        JSONObject jsonObject = JSON.parseObject(result);
        String ticket = jsonObject.getString("ticket");
        log.info("【用于获取ticket】 ：" + ticket);
        return ticket;
    }


    public WXTicketSignResponse share(String url, String appId, String appSecret) {
        WXTicketSignResponse signResponse = new WXTicketSignResponse();

        String accessToken = wxQrService.getAccessToken(appId, appSecret);
        if (!StringUtils.hasText(accessToken)) {
            MessageCode.ERROR.throwException("获取token失败");
        }
        String ticket = getTicket(accessToken, appId);
        if (!StringUtils.hasText(ticket)) {
            MessageCode.ERROR.throwException("获取ticket失败");
        }
        String nonceStr = RandomStringUtils.randomAlphanumeric(20);//随机字符串
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳
        String str = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
        String signature = DigestUtils.sha1Hex(str);
        signResponse.setNonceStr(nonceStr);
        signResponse.setSignature(signature);
        signResponse.setTimestamp(timestamp);
        signResponse.setAppId(appId);
        log.info("公众号分享:{}", JSONUtil.toJsonStr(signResponse));
        return signResponse;
    }

    //小程序登录获取openid session_key(用来解密出手机号)
    private Code2SessionResponse getSessionKeyOrOpenId(String code) {
        String url = MessageFormat.format(WxRequestUrlConstant.XCX_GET_OPENID, "appId", "AppSecret", code);
        RestTemplate rest = new RestTemplate();
        Map<String, Object> session = new HashMap<>();
        String sessionText = rest.getForObject(url, String.class);
        try {
            session = mapper.readValue(sessionText, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.info("sessionText: {}", sessionText);

        Code2SessionResponse code2SessionResponse = new Code2SessionResponse();
        code2SessionResponse.setOpenid((String) session.get("openid"));
        code2SessionResponse.setUnionid((String) session.get("unionid"));
        code2SessionResponse.setSessionKey((String) session.get("session_key"));
        if (StringUtils.hasText(code2SessionResponse.getOpenid())) {
            code2SessionResponse.setErrcode(0);
        } else {
            code2SessionResponse.setErrcode((Number) session.get("errcode"));
        }
        code2SessionResponse.setErrmsg((String) session.get("errmsg"));

        return code2SessionResponse;
    }


    /**
     * <desc>
     * 解密与微信绑定的手机号
     * </desc>
     *
     * @param encrypdata 微信参数
     * @param ivdata     微信参数
     * @param sessionKey 会话密钥
     * @return
     * @createDate 2018/11/24
     */
    public String getPhoneNumber(String encrypdata, String ivdata, String sessionKey) {
        log.debug("getPhoneNumber request: {}, {}, {}", encrypdata, ivdata, sessionKey);
        try {
            if (!StringUtils.hasText(encrypdata) || !StringUtils.hasText(ivdata) || !StringUtils.hasText(sessionKey)) {
                log.error("参数不全");
                return null;
            }
            byte[] encryptData = Base64.decode(encrypdata);
            byte[] ivData = Base64.decode(ivdata);
            byte[] sessionKeyByte = Base64.decode(sessionKey);
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivData);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(sessionKeyByte, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            //解析解密后的字符串
            String phoneNum = new String(cipher.doFinal(encryptData), "UTF-8");
            Map<String, Object> map = JSONObject.parseObject(phoneNum);
            if (CollectionUtil.isNotEmpty(map)) {
                phoneNum = (String) map.get("phoneNumber");
            }
            return phoneNum;
        } catch (Exception e) {
            log.error("getPhoneNumber error:", e);
        }
        return null;
    }
}
