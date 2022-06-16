package com.rabbit.jpush;

import lombok.Data;

import java.util.List;

/**
 * @author : yaobangqi
 * @version V1.0
 * @Project: union-central
 * @Package cn.vpclub.ubp.union.central.bff.admin.message.info.model.request.push
 * @Description: TODO
 * @date Date : 2021/5/14
 */
@Data
public class PushMessage {
    /**
     * 消息id
     */
    private String messageId;
    /**
     * 标题
     */
    private String title;
    /**
     * 消息内容
     */
    private String message;
    /**
     * 手机号集合
     */
    private List<String> phoneList;

    /**
     * 消息过期时间,其值不可小于发送时间
     * 格式同start_time
     */
    private Long expireTime;
    /**
     * 如果为type为html5则为短地址(链接地址)，type为native在为functionid(自定义的跳转路径)
     */
    private String target;

    //IOS Android All
    private String platform;

}
