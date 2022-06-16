package com.kafka.log.message;

import com.kafka.log.AppOperateLogMessage;
import com.kafka.log.handle.SendAppOperateLogHandler;

public class DefaultSendAppOperateLogMessage implements SendAppOperateLogMessage {

    private SendAppOperateLogHandler sendAppOperateLogHandler;


    public DefaultSendAppOperateLogMessage(SendAppOperateLogHandler sendAppOperateLogHandler) {
        this.sendAppOperateLogHandler = sendAppOperateLogHandler;
    }


    @Override
    public void handlerMessage(AppOperateLogMessage appOperateLogMessage) {
        sendAppOperateLogHandler.sendAppOperateLog(appOperateLogMessage);
    }
}
