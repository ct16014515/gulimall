package com.iflytek.gulimall.product.feign.factory;

import com.iflytek.common.exception.GulimallExceptinCodeEnum;
import com.iflytek.common.utils.ResultBody;
import com.iflytek.gulimall.product.feign.SearchFeignService;
import com.iflytek.gulimall.product.vo.SeckillSkuVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SearchServiceFallbackFactory implements FallbackFactory<SearchFeignService> {
    @Override
    public SearchFeignService create(Throwable throwable) {
        return new SearchFeignService(){
            @Override
            public ResultBody<SeckillSkuVo> getSkuSeckilInfo(Long skuId) {
                log.info("查询秒杀商品失败原因:{}",throwable.getMessage());
                ResultBody resultBody=new ResultBody(GulimallExceptinCodeEnum.FEIGN_ERROR,null);
                return resultBody;
            }
        } ;
    }
}
