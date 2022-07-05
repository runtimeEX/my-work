package com.boss.wx.message;

public enum MessageCode implements BaseCode {
    SUCCESS(1000, "成功"),
    ERROR(1001, "业务异常"),
    PARAM_ERROR(1002, "参数异常"),
    TOKEN_INVALID(1003, "登录状态过期"),
    SMS_SEND_ERROR(1004, "短信发送失败"),
    DISCARD_ERROR(1005, "版本过时"),
    PERMISSION_ERROR(1006, "权限不足"),
    SERVER_ERROR(1007, "服务器正在维护"),
    //sql异常时，message仍然提示服务器正在维护
    SQL_ERROR(1008, "服务器正在维护"),
    ;

    private final Integer returnCode;
    private final String message;

    MessageCode(Integer returnCode, String message) {
        this.returnCode = returnCode;
        this.message = message;
    }

    @Override
    public Integer getReturnCode() {
        return returnCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}