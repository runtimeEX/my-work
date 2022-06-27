package com.boss.wx.model.response;

import com.boss.wx.model.AbstractRS;
import lombok.Data;

@Data
public class WxH5OrderRS extends AbstractRS {
    /** 跳转地址 **/
    private String payUrl;

    /** 二维码地址 **/
    private String codeUrl;

    /** 二维码图片地址 **/
    private String codeImgUrl;

}
