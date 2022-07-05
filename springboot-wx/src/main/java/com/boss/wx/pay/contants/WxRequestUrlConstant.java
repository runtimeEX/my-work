package com.boss.wx.pay.contants;

public class WxRequestUrlConstant {
    /**
     * 小程序获取openid
     */
    public final static String XCX_GET_OPENID = "https://api.weixin.qq.com/sns/jscode2session?appid={0}&secret={1}&js_code={2}&grant_type=authorization_code";
    /**
     * 获取accesstoken（公众号需要配置ip白名单，公众号不需要）
     */
    public final static String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";
    /**
     * 获取小程序 scheme 码
     */
    public final static String SCHEME_URL = "https://api.weixin.qq.com/wxa/generatescheme?access_token={0}";

    /**
     * 公众号获取openid
     */
    public final static String GZH_GET_OPENID = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";
    /**
     * 公众号获取用户信息
     */
    public final static String GZH_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token={0}&openid={0}&lang=zh_CN";
    /**
     * 公众号获取分享ticket
     */
    public final static String GZH_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={0}&type=jsapi";

    /**
     * 获取小程序太阳码
     */
    public final static String QR_CODE = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token={0}";

    /**
     * 微信小程序access_token缓存key
     */
    public static final String ACCESS_TOKEN_KEY = "WX_AUTHORIZER_ACCESS_TOKEN";

}
