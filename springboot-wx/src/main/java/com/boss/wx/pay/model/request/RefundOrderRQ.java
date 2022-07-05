package com.boss.wx.pay.model.request;

import lombok.Data;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.model.request
 * @Description: TODO
 * @date Date : 2022年06月28日 下午1:59
 */
@Data
public class RefundOrderRQ {
    /** 商户系统生成的退款单号   **/
    private String mchRefundNo;
}
