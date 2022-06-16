package com.kafka.log.handle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.log.AppOperateLogMessage;
import com.kafka.log.message.ReceiveAppOperateLogMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
public class ReceiveAppOperateLogHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReceiveAppOperateLogMessage receiveAppOperateLogMessage;

    @KafkaListener(id = "app-operate-log", topics = "${app.operate.log.topic:}")
    public void listen(String jsonData) {
        try {
            AppOperateLogMessage appOperateLogMessage = objectMapper.readValue(jsonData, AppOperateLogMessage.class);
            receiveAppOperateLogMessage.handlerMessage(appOperateLogMessage);
        } catch (JsonProcessingException e) {
            log.info("接收处理操作日志json错误");
            e.printStackTrace();
        }
    }

}