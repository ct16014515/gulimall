package com.iflytek.gulimall.order.feign;

import com.iflytek.common.model.vo.cart.CartItemVO;
import com.iflytek.common.utils.ResultBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "gulimall-cart")
public interface CartService  {
    @GetMapping("/cart/getCartListByUid")
    public ResultBody<List<CartItemVO>> getCartListByUid(@RequestParam("uid") String uid);
}
