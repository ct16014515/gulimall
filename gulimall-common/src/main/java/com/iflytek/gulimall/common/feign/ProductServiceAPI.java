package com.iflytek.gulimall.common.feign;

import com.iflytek.gulimall.common.feign.vo.OrderItemVO;
import com.iflytek.gulimall.common.feign.vo.SkuInfoPriceVO;
import com.iflytek.gulimall.common.feign.vo.SkuInfoVO;
import com.iflytek.gulimall.common.utils.ResultBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "gulimall-product")
@Component
public interface ProductServiceAPI {

    @RequestMapping("/product/skuinfo/info/{skuId}")
    public ResultBody<SkuInfoVO> info(@PathVariable("skuId") Long skuId);

    @PostMapping("product/skuinfo/list")
    public ResultBody<List<SkuInfoVO>> list(@RequestBody List<Long> skuIds);

    @RequestMapping("/product/skuinfo/getskuAttrsBySkuId")
    public ResultBody<List<String>> getskuAttrsBySkuId(@RequestParam("skuId") Long skuId);


    @PostMapping("/product/skuinfo/getSkuPriceBySkuIds")
    public ResultBody<List<SkuInfoPriceVO>> getSkuPriceBySkuIds(@RequestBody List<Long> skuIds);
    @PostMapping("product/skuinfo/getOrderItemsBySkuIds")
    List<OrderItemVO> getOrderItemsBySkuIds(@RequestBody List<Long> skuIds);
}
