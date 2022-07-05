package com.boss.wx.message;

import com.boss.wx.exception.BusinessException;

public interface BaseCode {
    /**
     * 错误编码
     */
    Integer getReturnCode();

    /**
     * 描述信息
     */
    String getMessage();

    default void throwException(String message) throws BusinessException {
        throw new BusinessException(this.getReturnCode(), message);
    }

    default void throwException() throws BusinessException {
        throw new BusinessException(this);
    }

    default void throwException(BaseCode baseCode) throws BusinessException {
        throw new BusinessException(baseCode);
    }

    default BusinessException buildException() {
        return new BusinessException(this);
    }


    default BusinessException buildException(String retMsg) {
        return new BusinessException(this.getReturnCode(), retMsg);
    }

    default <T> MessageBody<T> build() {
        return new MessageBody<>(getReturnCode(), getMessage(), null);
    }

    default <T> MessageBody<T> data(T retData) {
        return new MessageBody<>(getReturnCode(), getMessage(), retData);
    }

    default <T> MessageBody<T> message(String message) {
        return new MessageBody<>(getReturnCode(), message, null);
    }
}
