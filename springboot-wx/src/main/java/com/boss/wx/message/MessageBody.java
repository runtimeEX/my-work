package com.boss.wx.message;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

@Data
public class MessageBody<T> {
    /**
     * 响应代码
     */
    private Integer returnCode;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 响应数据
     */
    private T dataInfo;

    public MessageBody() {
    }

    public MessageBody(Integer returnCode, String message, T dataInfo) {
        this.returnCode = returnCode;
        this.message = message;
        this.dataInfo = dataInfo;
    }

    public MessageBody(Integer returnCode, String message) {
        this.returnCode = returnCode;
        this.message = message;
    }

    /**
     * 是否成功
     *
     * @return true/false
     */
    public boolean isSuccess() {
        return MessageCode.SUCCESS.getReturnCode().equals(returnCode);
    }

    /**
     * 是否空数据
     *
     * @return true/false
     */
    public boolean isNullData() {
        return this.dataInfo == null;
    }

    /**
     * 是否空集合
     *
     * @return true/false
     */
    public boolean isEmptyCollection() {
        if (this.dataInfo == null) {
            return true;
        }
        if (!(this.dataInfo instanceof Collection)) {
            return true;
        }
        return CollectionUtils.isEmpty((Collection<?>) this.dataInfo);
    }

    /**
     * 成功并且数据不为空
     * @return true/false
     */
    public boolean isSuccessAndNotNullData() {
        return isSuccess() && !isNullData();
    }

    /**
     * 成功并且集合不为空
     * @return true/false
     */
    public boolean isSuccessAndNotEmptyCollection() {
        return isSuccess() && !isEmptyCollection();
    }
}