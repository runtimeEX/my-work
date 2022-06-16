package com.kafka.controller;

import com.kafka.log.aop.AppOperateLog;
import com.kafka.log.aop.AppOperateLogType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-kafka
 * @Package com.kafka.controller
 * @Description: TODO
 * @date Date : 2021年09月26日 下午5:18
 */
@RestController
@RequestMapping("/log")
public class OperateLogController {
    @GetMapping("/add")
    @Transactional
    @AppOperateLog(logTitle = "你和",operateType = AppOperateLogType.ADD)
    public String add(String log) {
        return log;
    }
}
