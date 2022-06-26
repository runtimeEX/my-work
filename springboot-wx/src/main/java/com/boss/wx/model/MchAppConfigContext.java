package com.boss.wx.model;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * * 商户应用支付参数信息
 * * 放置到内存， 避免多次查询操作
 */
@Data
public class MchAppConfigContext {
    private String mchNo;
    private String appId;
    /**
     * 商户类型
     */
    private Byte mchType;
    /**
     * 缓存 wxServiceWrapper 对象
     **/
    private WxServiceWrapper wxServiceWrapper;

    /**
     * 商户支付配置信息缓存,  <接口类型, 支付参数>
     */
    private Map<String, NormalMchParams> normalMchParamsMap = new ConcurrentHashMap<>();

    /**
     * 获取普通商户配置信息
     **/
    public NormalMchParams getNormalMchParamsByIfCode(String ifCode) {
        return normalMchParamsMap.get(ifCode);
    }

    /**
     * 获取普通商户配置信息
     **/
    public <T> T getNormalMchParamsByIfCode(String ifCode, Class<? extends NormalMchParams> cls) {
        return (T) normalMchParamsMap.get(ifCode);
    }
}
