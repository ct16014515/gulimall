package com.iflytek.gulimall.product.feign;

import com.iflytek.common.utils.ResultBody;
import com.iflytek.gulimall.product.feign.factory.SeckillServiceFallbackFactory;
import com.iflytek.gulimall.product.vo.SeckillSkuVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(value = "gulimall-seckill",fallbackFactory = SeckillServiceFallbackFactory.class )
public interface SeckillService {

    /**
     * 根据skuId查询商品是否参加秒杀活动
     * @param skuId
     * @return
     */
    @GetMapping(value = "/sku/seckill/{skuId}")
    ResultBody<SeckillSkuVo> getSkuSeckilInfo(@PathVariable("skuId") Long skuId);
}
