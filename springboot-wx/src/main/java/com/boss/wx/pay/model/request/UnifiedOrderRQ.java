package com.boss.wx.pay.model.request;

import com.boss.wx.pay.model.AbstractRQ;
import lombok.Data;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.model.request
 * @Description: TODO
 * @date Date : 2022年06月27日 下午3:01
 */
@Data
public class UnifiedOrderRQ extends AbstractRQ {
    /**
     * 订单应付金额(单位分)
     */
    private Long orderActualPrice;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 订单描述
     */
    private String description;
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 回调通知url
     */
    private String notifyUrl;
    /**
     * 用户终端ip
     */
    private String ip;
    /**
     * 支付接口
     */
    private String wayCode;

}
