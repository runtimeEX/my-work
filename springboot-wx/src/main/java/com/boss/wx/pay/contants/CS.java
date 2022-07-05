package com.boss.wx.pay.contants;

/**
 * @Description Constants 常量对象
 **/
public class CS {

    /**
     * 账号类型:1-服务商 2-商户 3-商户应用
     */
    public static final byte INFO_TYPE_ISV = 1;
    public static final byte INFO_TYPE_MCH = 2;
    public static final byte INFO_TYPE_MCH_APP = 3;


    /**
     * 商户类型:1-普通商户 2-特约商户
     */
    public static final byte MCH_TYPE_NORMAL = 1;
    public static final byte MCH_TYPE_ISVSUB = 2;

    public static final String SIGN_TYPE_RSA = "RSA";
    public static final String SIGN_TYPE_RSA2 = "RSA2";

    //支付宝
    /** 网关地址 */
    public static String PROD_SERVER_URL = "https://openapi.alipay.com/gateway.do";
    public static String SANDBOX_SERVER_URL = "https://openapi.alipaydev.com/gateway.do";

    public static String PROD_OAUTH_URL = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=%s&scope=auth_base&state=&redirect_uri=%s";
    public static String SANDBOX_OAUTH_URL = "https://openauth.alipaydev.com/oauth2/publicAppAuthorize.htm?app_id=%s&scope=auth_base&state=&redirect_uri=%s";

    /** isv获取授权商户URL地址 **/
    public static String PROD_APP_TO_APP_AUTH_URL = "https://openauth.alipay.com/oauth2/appToAppAuth.htm?app_id=%s&redirect_uri=%s&state=%s";
    public static String SANDBOX_APP_TO_APP_AUTH_URL = "https://openauth.alipaydev.com/oauth2/appToAppAuth.htm?app_id=%s&redirect_uri=%s&state=%s";


    public static String FORMAT = "json";

    public static String CHARSET = "UTF-8";


    /** yes or no **/
    public static final Integer NO = 0;
    public static final Integer YES = 1;
    //接口类型
    public interface IF_CODE {

        String ALIPAY = "alipay";   // 支付宝官方支付
        String WXPAY = "wxpay";     // 微信官方支付
        String YSFPAY = "ysfpay";   // 云闪付开放平台
        String XXPAY = "xxpay";     // 小新支付
        String PPPAY = "pppay";     // Paypal 支付
    }


    //支付方式代码
    public interface PAY_WAY_CODE {

        // 特殊支付方式
        String QR_CASHIER = "QR_CASHIER"; //  ( 通过二维码跳转到收银台完成支付， 已集成获取用户ID的实现。  )
        String AUTO_BAR = "AUTO_BAR"; // 条码聚合支付（自动分类条码类型）

        String ALI_BAR = "ALI_BAR";  //支付宝条码支付
        String ALI_JSAPI = "ALI_JSAPI";  //支付宝服务窗支付
        String ALI_APP = "ALI_APP";  //支付宝 app支付
        String ALI_PC = "ALI_PC";  //支付宝 电脑网站支付
        String ALI_WAP = "ALI_WAP";  //支付宝 wap支付
        String ALI_QR = "ALI_QR";  //支付宝 二维码付款

        String YSF_BAR = "YSF_BAR";  //云闪付条码支付
        String YSF_JSAPI = "YSF_JSAPI";  //云闪付服务窗支付

        String WX_JSAPI = "WX_JSAPI";  //微信jsapi支付
        String WX_LITE = "WX_LITE";  //微信小程序支付
        String WX_BAR = "WX_BAR";  //微信条码支付
        String WX_H5 = "WX_H5";  //微信H5支付
        String WX_APP = "WX_APP";//微信app支付
        String WX_NATIVE = "WX_NATIVE";  //微信扫码支付

        String PP_PC = "PP_PC"; // Paypal 支付
    }

    //支付数据包 类型
    public interface PAY_DATA_TYPE {
        String PAY_URL = "payurl";  //跳转链接的方式  redirectUrl
        String FORM = "form";  //表单提交
        String WX_APP = "wxapp";  //微信app参数
        String ALI_APP = "aliapp";  //支付宝app参数
        String YSF_APP = "ysfapp";  //云闪付app参数
        String CODE_URL = "codeUrl";  //二维码URL
        String CODE_IMG_URL = "codeImgUrl";  //二维码图片显示URL
        String NONE = "none";  //无参数
//        String QR_CONTENT = "qrContent";  //二维码实际内容
    }


    //接口版本
    public interface PAY_IF_VERSION {
        String WX_V2 = "V2";  //微信接口版本V2
        String WX_V3 = "V3";  //微信接口版本V3
    }
}
