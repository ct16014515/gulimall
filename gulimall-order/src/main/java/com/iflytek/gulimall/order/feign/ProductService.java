package com.iflytek.gulimall.order.feign;

import com.iflytek.common.model.vo.order.OrderItemVO;
import com.iflytek.common.model.vo.product.SkuInfoPriceVO;
import com.iflytek.common.utils.ResultBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimall-product")
public interface ProductService {


    @PostMapping("/product/skuinfo/getSkuPriceBySkuIds")
    public ResultBody<List<SkuInfoPriceVO>> getSkuPriceBySkuIds(@RequestBody List<Long> skuIds);
    @PostMapping("product/skuinfo/getOrderItemsBySkuIds")
    List<OrderItemVO> getOrderItemsBySkuIds(@RequestBody List<Long> skuIds);
}
