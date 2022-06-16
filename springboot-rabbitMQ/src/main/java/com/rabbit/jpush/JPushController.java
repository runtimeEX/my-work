package com.rabbit.jpush;

import cn.jpush.api.push.PushResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-rabbitMQ
 * @Package com.rabbit.jpush
 * @Description: TODO
 * @date Date : 2021年11月10日 下午2:49
 */
@RestController
@RequestMapping("/rb")
public class JPushController {
    @Autowired
    private JPushHandler jPushHandler;
    @Autowired
    private JPushMessageClient jPushMessageClient;

    @PostMapping("/push")
    public Boolean push(@RequestBody PushMessage req) {
        return jPushHandler.sendPush(req);

    }

    @GetMapping("/p")
    public PushResult p(){
        List<String> aliasList = Arrays.asList("17807534308");
        String notificationTitle = "浏览了小程序，请及时关注！";
        String msgTitle = "浏览提醒";
        String msgContent = "浏览了小程序，请及时关注！";
        PushResult pushResult = jPushMessageClient.sendToAliasList(aliasList, notificationTitle, msgTitle, msgContent, "exts");
        return pushResult;
    }
    @PostMapping("/x")
    public List<Integer> x(@RequestBody List<Integer> list){
        return list;
    }
}
