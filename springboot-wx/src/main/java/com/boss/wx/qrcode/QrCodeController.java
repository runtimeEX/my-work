package com.boss.wx.qrcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-wx
 * @Package com.boss.wx.qrcode
 * @Description: TODO
 * @date Date : 2021年09月24日 下午6:14
 */
@RestController
@RequestMapping("/code")
public class QrCodeController {
    @Autowired
    private QrCodeService qrCodeService;
    @RequestMapping("/add")
    public String code(String token){
        String qrCode = qrCodeService.getQrCode(token, 1231l);
        return qrCode;
    }
}
