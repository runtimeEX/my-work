package com.boss.wx.pay.model;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public abstract class AbstractRS implements Serializable {
    public String toJSONString() {
        return JSON.toJSONString(this);
    }
}
