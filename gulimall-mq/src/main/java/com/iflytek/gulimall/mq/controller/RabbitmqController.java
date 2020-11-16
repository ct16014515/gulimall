package com.iflytek.gulimall.mq.controller;
import com.iflytek.gulimall.common.feign.MqServiceAPI;
import com.iflytek.gulimall.common.feign.vo.SendMessageRequest;
import com.iflytek.gulimall.common.utils.ResultBody;

import com.iflytek.gulimall.mq.service.RabbitTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rabbitmq")
public class RabbitmqController implements MqServiceAPI {

    @Autowired
    private RabbitTemplateService rabbitTemplateService;

    @PostMapping("/sendMessage")
    public ResultBody sendMessage(@RequestBody SendMessageRequest sendMessageRequest)  {
        try {
            rabbitTemplateService.sendMessage(sendMessageRequest);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ResultBody();
    }

    @GetMapping("/listener/{messageId}")
    public ResultBody updateMessageStatus(@PathVariable("messageId") String messageId) {
        rabbitTemplateService.updateMessageStatus(messageId);
        return new ResultBody();
    }


}
