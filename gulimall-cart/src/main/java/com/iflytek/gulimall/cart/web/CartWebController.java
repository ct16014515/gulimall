package com.iflytek.gulimall.cart.web;

import com.iflytek.gulimall.common.feign.vo.CartItemVO;
import com.iflytek.gulimall.cart.service.CartService;
import com.iflytek.gulimall.cart.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.ExecutionException;

@Controller
public class CartWebController {

    @Autowired
    CartService cartService;


    /**
     * 1、从session里面判断是临时用户还是登陆用户
     * 2、如果是临时用户会知自动创建一个user-key存放在cookie. 从redis中获取临时购物车
     * 3、如果是登陆用户获取登陆用户的购物车合并临时购物车
     *
     * @return
     */
    @GetMapping("cart.html")
    public String cartList(Model model) {
        CartVO cartVO = cartService.cartList();
        model.addAttribute("cartVO", cartVO);
        return "cartlist";
    }

    //添加购物车后再次刷新会导致重复添加购物车,需要重定向到新的页面,再从新的页面查询数据返回给视图
    @GetMapping("addToCart.html")
    public String addToCart(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num,
                            RedirectAttributes redirectAttributes) throws ExecutionException, InterruptedException {
        cartService.addToCart(skuId, num);
        redirectAttributes.addAttribute("skuId", skuId);
        redirectAttributes.addAttribute("num", num);
        return "redirect:http://cart.gulimall.com/success.html";
    }

    @GetMapping("/success.html")
    public String loginHtml(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num,
                            Model model) {
        CartItemVO cartItemVO = cartService.getCarItemVOFromRedis(skuId);
        model.addAttribute("cartItemVO", cartItemVO);
        return "success";
    }

    @GetMapping("/changeCheck")
    public String changeCheck(@RequestParam("skuId") String skuId,
                              @RequestParam("checked") Integer checked) {
        cartService.changeCheck(skuId, checked);

        return "redirect:http://cart.gulimall.com/cart.html";
    }

    @GetMapping("/changeSkuCount")
    public String changeSkuCount(@RequestParam("skuId") String skuId,
                                 @RequestParam("skuCount") Integer skuCount) {
        cartService.changeSkuCount(skuId, skuCount);

        return "redirect:http://cart.gulimall.com/cart.html";
    }

    @GetMapping("/deleteItemCartVO")
    public String deleteItemCartVO(@RequestParam("skuId") String skuId) {
        cartService.deleteItemCartVO(skuId);
        return "redirect:http://cart.gulimall.com/cart.html";
    }


}
