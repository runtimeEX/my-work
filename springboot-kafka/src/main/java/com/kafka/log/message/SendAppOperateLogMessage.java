package com.kafka.log.message;

import com.kafka.log.AppOperateLogMessage;

public interface SendAppOperateLogMessage {

    void handlerMessage(AppOperateLogMessage appOperateLogMessage);

}
