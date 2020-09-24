package com.iflytek.gulimall.order.feign;


import com.iflytek.common.model.vo.product.WareSkuLockVO;
import com.iflytek.common.utils.R;
import com.iflytek.common.utils.ResultBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimall-ware")
public interface WareService {
    @GetMapping("ware/waresku/hasStock/{skuId}")
    public ResultBody<Integer> hasStockById(@PathVariable("skuId") Long skuId);

    @PostMapping("ware/waresku/hasStock")
    public R hasStock(@RequestBody List<Long> skuIds);

    @PostMapping("ware/waresku/wareSkuLock")
    public ResultBody wareSkuLock(@RequestBody WareSkuLockVO wareSkuLockVO);

}
