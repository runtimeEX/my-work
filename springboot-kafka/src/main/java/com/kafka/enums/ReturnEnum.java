package com.kafka.enums;

public enum ReturnEnum {

    CODE_2000(2000, "message.password.forget.need.match"),//两次输入的密码不匹配
    CODE_2001(2001, "message.login.account.need.apply"),//请申请crm账号
    CODE_2002(2002, "message.password.reset.failed"),//服务密码重置失败
    CODE_2003(2003, "message.password.modify.failed"),//服务密码修改失败
    CODE_2004(2004, "message.verification.code.error"),//验证码错误，请重新输入
    CODE_2005(2005, "message.password.error"),//用户名或密码错误，请重新输入
    CODE_2006(2006, "message.account.not.exist"),//帐号不存在
    CODE_2007(2007, "message.account.unavailable"),//帐号处于不可用状态
    CODE_2008(2008, "message.des.decode.failed"),//密码解密失败
    CODE_2009(2009, "message.job.number.not.exist"),//未查询到该账号下工号信息，暂无法登录
    CODE_2010(2010, "message.number.not.shandong.mobile"),
    CODE_2011(2011, "message.user.info.not.exist");

    private Integer code;
    private String value;

    ReturnEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
