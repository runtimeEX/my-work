package com.kafka.log.handle;

import com.alibaba.fastjson.JSON;
import com.kafka.log.AppOperateLogMessage;
import com.kafka.log.message.ReceiveAppOperateLogMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-kafka
 * @Package com.kafka.log.handle
 * @Description: TODO
 * @date Date : 2021年10月09日 下午5:08
 */
@Slf4j
@Component
public class AHandle implements ReceiveAppOperateLogMessage {
    @Override
    public void handlerMessage(AppOperateLogMessage appOperateLogMessage) {
        log.info("AHandle接收到操作日志记录 {} ", JSON.toJSONString(appOperateLogMessage));
    }
}
