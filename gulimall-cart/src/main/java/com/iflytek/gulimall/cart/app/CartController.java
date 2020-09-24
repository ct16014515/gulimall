package com.iflytek.gulimall.cart.app;

import com.iflytek.common.model.vo.cart.CartItemVO;
import com.iflytek.common.utils.ResultBody;
import com.iflytek.gulimall.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/getCartListByUid")
    public ResultBody<List<CartItemVO>> getCartListByUid(@RequestParam("uid") String uid) {
        //ResultBody<List<CartItemVO>> resultBody = cartService.getCartListByUid(uid);
        ResultBody<List<CartItemVO>> resultBody = cartService.getCartList();
        return resultBody;
    }

}
