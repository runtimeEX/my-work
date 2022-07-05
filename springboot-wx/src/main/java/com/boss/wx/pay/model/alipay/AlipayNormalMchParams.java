package com.boss.wx.pay.model.alipay;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.boss.wx.pay.model.NormalMchParams;
import com.boss.wx.pay.utils.StringKit;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Package cn.vpclub.ghs.bff.pay.model.alipay
 * @Description: TODO
 * @date Date : 2022年07月04日 下午4:11
 */
@Data
public class AlipayNormalMchParams extends NormalMchParams {
    /**
     * 是否沙箱环境
     */
    private Integer sandbox;

    /**
     * appId
     */
    private String appId;

    /**
     * privateKey
     */
    private String privateKey;

    /**
     * alipayPublicKey
     */
    private String alipayPublicKey;

    /**
     * 签名方式
     **/
    private String signType;

    /**
     * 是否使用证书方式
     **/
    private Integer useCert;

    /**
     * app 证书
     **/
    private String appPublicCert;

    /**
     * 支付宝公钥证书（.crt格式）
     **/
    private String alipayPublicCert;

    /**
     * 支付宝根证书
     **/
    private String alipayRootCert;

    @Override
    public String deSenData() {

        AlipayNormalMchParams mchParams = this;
        if (StringUtils.isNotBlank(this.privateKey)) {
            mchParams.setPrivateKey(StringKit.str2Star(this.privateKey, 4, 4, 6));
        }
        if (StringUtils.isNotBlank(this.alipayPublicKey)) {
            mchParams.setAlipayPublicKey(StringKit.str2Star(this.alipayPublicKey, 6, 6, 6));
        }
        return ((JSONObject) JSON.toJSON(mchParams)).toJSONString();
    }

}
