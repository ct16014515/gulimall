package com.iflytek.gulimall.product.feign;


import com.iflytek.common.model.vo.WareHasStockVo;
import com.iflytek.common.utils.R;
import com.iflytek.common.utils.ResultBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimall-ware")
public interface WareService {
    @PostMapping("ware/waresku/hasStock")
    public R hasStock(@RequestBody List<Long> skuIds);
}
