package com.shiro.utils.parm;

public interface ICommonParam {
    /**
     * @return 约定的客户端id
     */
    String getAppId();

    /**
     * 约定的客户端id
     */
    void setAppId(String appId);

    /**
     * @return 请求发起时间，13位时间戳
     */
    Long getTimestamp();

    /**
     * 请求发起时间，13位时间戳
     */
    void setTimestamp(Long timestamp);

    /**
     * @return 客户端设备类型
     */
    String getDeviceType();

    /**
     * 客户端设备类型
     */
    void setDeviceType(String deviceType);

    /**
     * @return 展示版本号，如：1.0.0
     */
    String getVersion();

    /**
     * 展示版本号，如：1.0.0
     */
    void setVersion(String version);

    /**
     * @return 构建版本号，如：10
     */
    String getVersionCode();

    /**
     * 构建版本号，如：10
     */
    void setVersionCode(String versionCode);

    /**
     * @return 32位UUID，用于打通全平台链路追踪
     */
    String getChainId();

    /**
     * 32位UUID，用于打通全平台链路追踪
     */
    void setChainId(String chainId);

    /**
     * @return 客户端设备类型,合并后，如android、ios合并为app
     */
    String getCombineDeviceType();

    /**
     * 客户端设备类型,合并后，如android、ios合并为app
     */
    void setCombineDeviceType(String combineDeviceType);
}