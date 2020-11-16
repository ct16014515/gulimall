package com.iflytek.gulimall.common.feign;

import com.iflytek.gulimall.common.utils.ResultBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 优惠价服务
 */
@FeignClient(value = "gulimall-coupon")
public interface CouponServiceAPI {
    @GetMapping("coupon/seckillsession/uploadSeckillProductLast3Days")
    public ResultBody uploadSeckillProductLast3Days();

}
