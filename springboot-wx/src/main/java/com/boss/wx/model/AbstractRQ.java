package com.boss.wx.model;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
@Data
public abstract class AbstractRQ implements Serializable {
    public String toJSONString(){
        return JSON.toJSONString(this);
    }
}
