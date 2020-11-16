package com.iflytek.gulimall.thirdparty.controller;

import com.iflytek.gulimall.common.feign.ThirdPartyServiceAPI;
import com.iflytek.gulimall.common.utils.ResultBody;

import com.iflytek.gulimall.thirdparty.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("thirdparty")
public class SmsController implements ThirdPartyServiceAPI {
    @Autowired
    private SmsComponent smsComponent;

    @GetMapping("/sendMsg")
    public ResultBody sendMsg(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        return smsComponent.sendMessage(code, phone);
    }


}
