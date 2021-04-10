package com.iflytek.gulimall.cart.app;

import com.iflytek.gulimall.common.feign.CartServiceAPI;
import com.iflytek.gulimall.common.feign.vo.CartItemVO;
import com.iflytek.gulimall.cart.service.CartService;

import com.iflytek.gulimall.common.utils.ResultBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController implements CartServiceAPI {
    @Autowired
    private CartService cartService;

    @GetMapping("/getCartListByUid")
    public ResultBody<List<CartItemVO>> getCartListByUid(@RequestParam("uid") String uid) {
        return cartService.getCartList();
    }

}
