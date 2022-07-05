package com.boss.wx.pay.model.response;

import com.boss.wx.pay.model.AbstractRS;
import lombok.Data;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.model.response
 * @Description: TODO
 * @date Date : 2022年06月30日 下午5:43
 */
@Data
public class WxNativeOrderRS extends AbstractRS {
    /**
     * 二维码地址
     **/
    private String codeUrl;
}
