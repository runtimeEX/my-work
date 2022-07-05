package com.boss.wx.pay.utils;

import cn.hutool.core.util.StrUtil;
import com.boss.wx.pay.channel.IPaymentService;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.utils
 * @Description: TODO
 * @date Date : 2022年06月27日 下午5:44
 */
public class PaywayUtil {
    private static final String PAYWAY_PACKAGE_NAME = "payway";
    private static final String PAYWAYV3_PACKAGE_NAME = "paywayV3";

    /**
     * 获取真实的支付方式Service
     **/
    public static IPaymentService getRealPaywayService(Object obj, String wayCode) {

        try {

            //下划线转换驼峰 & 首字母大写
            String clsName = StrUtil.upperFirst(StrUtil.toCamelCase(wayCode.toLowerCase()));
            return (IPaymentService) SpringBeansUtil.getBean(
                    Class.forName(obj.getClass().getPackage().getName()
                            + "." + PAYWAY_PACKAGE_NAME
                            + "." + clsName)
            );

        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * 获取微信V3真实的支付方式Service
     **/
    public static IPaymentService getRealPaywayV3Service(Object obj, String wayCode) {

        try {

            //下划线转换驼峰 & 首字母大写
            String clsName = StrUtil.upperFirst(StrUtil.toCamelCase(wayCode.toLowerCase()));
            String s = obj.getClass().getPackage().getName()
                    + "." + PAYWAYV3_PACKAGE_NAME
                    + "." + clsName;
            return (IPaymentService) SpringBeansUtil.getBean(
                    Class.forName(s)
            );

        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
