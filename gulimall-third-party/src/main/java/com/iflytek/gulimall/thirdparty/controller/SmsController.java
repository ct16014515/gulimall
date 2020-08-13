package com.iflytek.gulimall.thirdparty.controller;

import com.iflytek.common.utils.ResultBody;
import com.iflytek.gulimall.thirdparty.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("thirdparty")
public class SmsController {
    @Autowired
    private SmsComponent smsComponent;

    @GetMapping("/sendMsg")
    public ResultBody sendMsg(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        return smsComponent.sendMessage(code, phone);
    }


}
