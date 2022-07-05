package com.boss.wx.pay.model.response;

import com.boss.wx.pay.model.AbstractRS;
import lombok.Data;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.model.response
 * @Description: TODO
 * @date Date : 2022年06月27日 下午4:35
 */
@Data
public class WxH5OrderRS extends AbstractRS {
    /** 跳转地址 **/
    private String payUrl;

    /** 二维码地址 **/
    private String codeUrl;

    /** 二维码图片地址 **/
    private String codeImgUrl;

}