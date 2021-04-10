package com.gulimall.testredislistener.controller;

import com.gulimall.testredislistener.service.CereBuyerCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {


    @Autowired
    CereBuyerCouponService cereBuyerCouponService;
    @GetMapping("/login.html")
    public void   createOrder(){
        cereBuyerCouponService.createOrder();
    }
}
