package com.iflytek.gulimall.common.feign;

import com.iflytek.gulimall.common.feign.vo.SendMessageRequest;

import com.iflytek.gulimall.common.utils.ResultBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gulimall-mq")
public interface MqServiceAPI {

    @PostMapping("rabbitmq/sendMessage")
    public ResultBody sendMessage(@RequestBody SendMessageRequest sendMessageRequest) ;

    @GetMapping("rabbitmq/listener/{messageId}")
    public ResultBody updateMessageStatus(@PathVariable("messageId") String messageId);
}
