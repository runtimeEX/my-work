package com.shiro.utils.parm.impl;

import com.shiro.utils.parm.ICommonParam;
import lombok.Data;

@Data
public class BaseCommonParam implements ICommonParam {
    /**
     * 约定的客户端id
     */
    private String appId;
    /**
     * 请求发起时间，13位时间戳
     */
    private Long timestamp;
    /**
     * 客户端设备类型
     */
    private String deviceType;
    /**
     * 展示版本号，如：1.0.0
     */
    private String version;
    /**
     * 构建版本号，如：10
     */
    private String versionCode;
    /**
     * 32位UUID，用于打通全平台链路追踪
     */
    private String chainId;
    /**
     * 客户端设备类型,合并后，如android、ios合并为app
     */
    private String combineDeviceType;
}
