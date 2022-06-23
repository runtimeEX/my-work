package com.boss.wx;

public interface IPaymentService {

    /** 获取到接口code **/
    String getIfCode();

    /** 是否支持该支付方式 */
    boolean isSupport(String wayCode);
}
