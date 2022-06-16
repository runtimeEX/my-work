package com.kafka.log.message;

import com.kafka.log.AppOperateLogMessage;

public interface ReceiveAppOperateLogMessage {

    void handlerMessage(AppOperateLogMessage appOperateLogMessage);

}