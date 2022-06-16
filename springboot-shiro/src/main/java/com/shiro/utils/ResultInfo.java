package com.shiro.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.utils
 * @Description: TODO
 * @date Date : 2021年06月18日 上午9:58
 */
@Data
public class ResultInfo implements Serializable {
    private static final long serialVersionUID = -4423979600706797978L;
    // 状态码
    private Integer code;
    // 消息
    private String message;
    // 数据对象
    private Object result;


    /**
     * 无参构造器
     */
    public ResultInfo() {
        super();
    }

    public ResultInfo(Status status) {
        super();
        this.code = status.code;
        this.message = status.message;
    }

    public ResultInfo result(Object result) {
        this.result = result;
        return this;
    }

    public ResultInfo message(String message) {
        this.message = message;
        return this;
    }

    /**
     * 只返回状态，状态码，消息
     *
     * @param code
     * @param message
     */
    public ResultInfo(Integer code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    /**
     * 只返回状态，状态码，数据对象
     *
     * @param code
     * @param result
     */
    public ResultInfo(Integer code, Object result) {
        super();
        this.code = code;
        this.result = result;
    }

    /**
     * 返回全部信息即状态，状态码，消息，数据对象
     *
     * @param code
     * @param message
     * @param result
     */
    public ResultInfo(Integer code, String message, Object result) {
        super();
        this.code = code;
        this.message = message;
        this.result = result;
    }
    /**
     * 返回全部信息即状态，状态码，消息，数据对象
     *
     * @param code
     * @param message
     * @param result
     */
    public ResultInfo(Status status, Object result) {
        super();
        this.code = status.code;
        this.message = status.message;
        this.result = result;
    }
}
