package com.iflytek.gulimall.common.feign;

import com.iflytek.gulimall.common.feign.vo.CartItemVO;

import com.iflytek.gulimall.common.utils.ResultBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "gulimall-cart")
public interface CartServiceAPI {
    @GetMapping("/cart/getCartListByUid")
    public ResultBody<List<CartItemVO>> getCartListByUid(@RequestParam("uid") String uid);
}
