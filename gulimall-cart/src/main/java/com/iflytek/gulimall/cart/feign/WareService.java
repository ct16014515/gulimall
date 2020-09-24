package com.iflytek.gulimall.cart.feign;


import com.iflytek.common.utils.ResultBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("gulimall-ware")
public interface WareService {
    @GetMapping("ware/waresku/hasStock/{skuId}")
    public ResultBody<Integer> hasStockById(@PathVariable("skuId") Long skuId);
}
