package com.kafka.controller;

import com.kafka.enums.ReturnEnum;
import com.kafka.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-kafka
 * @Package com.kafka.controller
 * @Description: TODO
 * @date Date : 2021年04月15日 下午5:34
 */
@RestController
public class KafkaController {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    // 发送消息
    @GetMapping("/kafka/{message}")
    @Transactional
    public void sendMessage1(@PathVariable("message") String message) {
        System.out.println("start===============>");
        try {
            kafkaTemplate.send("boss", message).get(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }


    @GetMapping("/kafka/callbackOne/{message}")
    @Transactional
    public void sendMessage2(@PathVariable("message") String callbackMessage) {
        //异步投递
        kafkaTemplate.send("boss", callbackMessage).addCallback(new SuccessCallback<SendResult>() {
            @Override
            public void onSuccess(SendResult success) {
                // 消息发送到的topic
                String topic = success.getRecordMetadata().topic();
                // 消息发送到的分区
                int partition = success.getRecordMetadata().partition();
                // 消息在分区内的offset
                long offset = success.getRecordMetadata().offset();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("发送消息成功:" + topic + "-" + partition + "-" + offset);
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(Throwable failure) {
                System.out.println("发送消息失败:" + failure.getMessage());
            }
        });
    }

    @GetMapping("/kafka/callbackTwo/{message}")
    public void sendMessage3(@PathVariable("message") String callbackMessage) {
        kafkaTemplate.send("topic1", callbackMessage).addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("发送消息失败：" + ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                System.out.println("发送消息成功：" + result.getRecordMetadata().topic() + "-"
                        + result.getRecordMetadata().partition() + "-" + result.getRecordMetadata().offset());
            }
        });
    }


    @GetMapping("/kafka/transaction")
    public void sendMessage7() {
        Object o = kafkaTemplate.executeInTransaction(kafkaOperations -> {
            kafkaOperations.send("topic1", "事务");
            throw new RuntimeException("fail");
            //  int i = 1 / 0;
            //   return null;
        });


    }

    @GetMapping("/batch")
    @Transactional
    public void batch() {
        for (int i = 1; i < 500; i++) {
            kafkaTemplate.send("topic1", "batch:" + i);
        }

    }

    @GetMapping("/mes")
    public Response mes() {
        return new Response().setMessage(ReturnEnum.CODE_2000.getValue());
        // return ReturnEnum.CODE_2000.getValue();
    }
}
