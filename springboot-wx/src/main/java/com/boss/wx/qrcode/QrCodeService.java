package com.boss.wx.qrcode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-wx
 * @Package com.boss.wx.qrcode
 * @Description: TODO
 * @date Date : 2021年09月24日 下午5:59
 */
@Service
@Slf4j
public class QrCodeService {
    @Autowired
    private IUploadService iUploadService;
    @Value("${app.wx.app-id}")
    private String appId;
    @Value("${app.wx.app-secret}")
    private String appSecret;
    @Value("${app.wx.accesstoken_url}")
    private String accesstokenUrl;
    @Value("${app.wx.qrcode_url}")
    private String qrCodeUrl;
    public String getQrCode(String accesstoken, Long shareRecordId) {
        String resultUrl = "";
        RestTemplate rest = new RestTemplate();
        InputStream inputStream = null;

        try {
            String url = MessageFormat.format(this.qrCodeUrl, accesstoken);
            log.info("调用生成微信二维码url：" + url);
            Map<String, Object> param = new HashMap<>();
            param.put("scene", shareRecordId.toString());
          //  param.put("page", "pages/scheme/index");
            param.put("width", 430);
            param.put("auto_color", false);
            Map<String, Object> line_color = new HashMap<>();
            line_color.put("r", 0);
            line_color.put("g", 0);
            line_color.put("b", 0);
            param.put("line_color", line_color);
            log.info("调用生成微信URL接口传参:" + param);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            HttpEntity requestEntity = new HttpEntity(param, headers);
            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
            byte[] result = entity.getBody();
            inputStream = new ByteArrayInputStream(result);
            String fileName = System.currentTimeMillis() + ".png";
            UploadRequest uploadRequest = new UploadRequest();
            uploadRequest.setFileName(fileName);
            uploadRequest.setTimestamp(System.currentTimeMillis());
            UploadSuccess uploadSuccess = iUploadService.upload(inputStream, uploadRequest);
            resultUrl = uploadSuccess.getUrl();
        } catch (Exception e) {
            log.error("调用小程序生成微信永久小程序码URL接口异常", e);
        }
        return resultUrl;

    }
}
