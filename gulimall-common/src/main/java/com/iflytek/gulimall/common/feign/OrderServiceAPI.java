package com.iflytek.gulimall.common.feign;

import com.iflytek.gulimall.common.feign.vo.OrderEntityVO;

import com.iflytek.gulimall.common.utils.PageUtils;
import com.iflytek.gulimall.common.utils.ResultBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient("gulimall-order")
public interface OrderServiceAPI {
    @PostMapping("/order/order/orderWithOrderItemList")
    public  ResultBody<PageUtils> orderWithOrderItemList(@RequestBody Map<String, Object> params);
    @GetMapping("order/order/getOrderEntityByOrderSn/{orderSn}")
    public ResultBody<OrderEntityVO> getOrderEntityByOrderSn(@PathVariable("orderSn") String orderSn);
}
