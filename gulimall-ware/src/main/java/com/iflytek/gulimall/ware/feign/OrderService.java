package com.iflytek.gulimall.ware.feign;

import com.iflytek.common.model.vo.order.OrderEntityVO;
import com.iflytek.common.utils.ResultBody;
import com.iflytek.gulimall.ware.vo.WareStockDelayVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("gulimall-order")
public interface OrderService {


    @GetMapping("order/order/getOrderEntityByOrderSn/{orderSn}")
    public ResultBody<OrderEntityVO> getOrderEntityByOrderSn(@PathVariable("orderSn") String orderSn);


}
