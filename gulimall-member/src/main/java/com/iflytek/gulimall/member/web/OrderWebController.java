package com.iflytek.gulimall.member.web;


import com.iflytek.gulimall.common.feign.OrderServiceAPI;
import com.iflytek.gulimall.common.utils.PageUtils;
import com.iflytek.gulimall.common.utils.ResultBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class OrderWebController {
    @Autowired
    OrderServiceAPI orderServiceAPI;

    @GetMapping("/myOrderList.html")
    public String myOrderList(@RequestParam(value = "page", defaultValue = "1") String page,
                              @RequestParam(value = "limit", defaultValue = "10") String limit,
                              Model model) {
        Map<String, Object> param = new HashMap<>();
        param.put("page", page);
        param.put("limit", limit);
        ResultBody<PageUtils> resultBody = orderServiceAPI.orderWithOrderItemList(param);
        PageUtils pageBean = resultBody.getData();
        model.addAttribute("pageBean",pageBean);
        return "orderList";
    }


}
