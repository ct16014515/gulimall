package com.iflytek.gulimall.common.feign;

import com.iflytek.gulimall.common.feign.vo.SkuReductionVO;
import com.iflytek.gulimall.common.feign.vo.SpuBoundVO;
import com.iflytek.gulimall.common.utils.ResultBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 优惠价服务
 */
@FeignClient(value = "gulimall-coupon")
public interface CouponServiceAPI {

    @PostMapping("/coupon/spubounds/save")
    void saveSpuBounds(SpuBoundVO spuBoundVO);
    @PostMapping("/coupon/spubounds/saveSkuFullReduction")
    void saveSkuReduction(SkuReductionVO skuReductionVO);
}
