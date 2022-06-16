package com.kafka.log.handle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.log.AppOperateLogMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Data
@Slf4j
public class SendAppOperateLogHandler {

    private String topicName;

    private ObjectMapper objectMapper;

    private KafkaTemplate<Object, Object> template;

    public SendAppOperateLogHandler(String topicName, ObjectMapper objectMapper, KafkaTemplate<Object, Object> template) {
        this.topicName = topicName;
        this.objectMapper = objectMapper;
        this.template = template;
    }


    public void sendAppOperateLog(AppOperateLogMessage appOperateLogMessage) {
        try {
            String dataJson = objectMapper.writeValueAsString(appOperateLogMessage);
            log.info("发送操作日志信息 {}", dataJson);
            this.template.send(topicName, dataJson);
        } catch (JsonProcessingException e) {
            log.error("转换JSON失败");
            e.printStackTrace();
        }
    }

}