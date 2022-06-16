package com.rabbit.mq.controller;

import com.rabbit.mq.producer.*;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleMsgController {
    @Autowired
    private DirectProducer directProducer;
    @Autowired
    private StringProducer stringProducer;
    @Autowired
    private TopicProducer topicProducer;
    @Autowired
    private FanoutProducer fanoutProducer;
    @Autowired
    private DelayProducer delayProducer;

    @Autowired
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @GetMapping("/start")
    public String start(String id) {
        MessageListenerContainer container = rabbitListenerEndpointRegistry.getListenerContainer(id);
        if (!container.isRunning()) {
            container.start();
        }
        return "start";
    }

    @GetMapping("/end")
    public String end(String id) {
        MessageListenerContainer container = rabbitListenerEndpointRegistry.getListenerContainer(id);
        if (container.isRunning()) {
            container.stop();
        }
        return "start";
    }

    @GetMapping("/sendMsg")
    public String sendMsg() {
        for (int i = 0; i < 3; i++) {
            directProducer.sendMsg("msg发消息" + i);
        }
        return "success";
    }

    @GetMapping("/sendString")
    public String sendString() {
        for (int i = 0; i < 50; i++) {
            stringProducer.sendMsg("string发消息" + i);
        }
        //  stringProducer.sendMsg("string发消息");
        return "success";
    }

    @GetMapping("/sendSimple")
    public String sendSimple() {
        for (int i = 1; i < 5; i++) {
            topicProducer.sendMsg("topicProducer发消息" + i);
        }
        //  topicProducer.sendMsg("simple发消息");
        return "success";
    }

    @GetMapping("/sendFanout")
    public String sendFanout() {
        for (int i = 1; i < 5; i++) {
            fanoutProducer.sendMsg("fanoutProducer发消息" + i);
        }
        //  simpleProducer.sendMsg("simple发消息");
        return "success";
    }

}
