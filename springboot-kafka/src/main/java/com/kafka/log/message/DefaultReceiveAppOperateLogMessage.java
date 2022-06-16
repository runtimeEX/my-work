package com.kafka.log.message;

import com.alibaba.fastjson.JSON;
import com.kafka.log.AppOperateLogMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultReceiveAppOperateLogMessage implements ReceiveAppOperateLogMessage {
    @Override
    public void handlerMessage(AppOperateLogMessage appOperateLogMessage) {
        log.info("接收到操作日志记录 {} ", JSON.toJSONString(appOperateLogMessage));
    }
}
