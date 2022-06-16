package com.kafka.response;

import java.io.Serializable;

public class Response<T> implements Serializable {

    private static final long serialVersionUID = 5550834913287227773L;
    /**
     * 请求结果
     */
    private Integer returnCode;
    /**
     * 错误信息
     */
    private String message;

    /**
     * 操作数据信息(请求结果成功返回)
     */
    private T dataInfo;



    public T getDataInfo() {
        return dataInfo;
    }

    public Integer getReturnCode() {
        return returnCode;
    }

    public Response setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Response setMessage(String message) {
        this.message = message;
        return this;
    }


    public Response<T> setDataInfo(T dataInfo) {
        this.dataInfo = dataInfo;
        return this;
    }

    @Override
    public String toString() {
        return "Response{" +
                "returnCode=" + returnCode +
                ", message='" + message + '\'' +
                ", dataInfo=" + dataInfo +
                '}';
    }
}