package com.boss.wx.exception;

import com.boss.wx.message.BaseCode;
import com.boss.wx.message.MessageBody;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {

    private MessageBody<?> messageBody;

    public BusinessException() {
        super();
    }

    public BusinessException(MessageBody<?> messageBody) {
        super(messageBody.getMessage());
        this.messageBody = messageBody;
    }

    public BusinessException(Integer returnCode, String message) {
        super(message);
        this.messageBody = new MessageBody<>(returnCode, message);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(BaseCode baseCode) {
        super(baseCode.getMessage());
        this.messageBody = new MessageBody<>(baseCode.getReturnCode(), baseCode.getMessage());
    }

}