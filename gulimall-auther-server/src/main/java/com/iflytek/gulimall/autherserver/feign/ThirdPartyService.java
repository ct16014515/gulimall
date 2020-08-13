package com.iflytek.gulimall.autherserver.feign;

import com.iflytek.common.utils.ResultBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "gulimall-third-party")
public interface ThirdPartyService {


    @GetMapping("thirdparty/sendMsg")
    public ResultBody sendMsg(@RequestParam("phone") String phone, @RequestParam("code") String code);

}
