package com.iflytek.gulimall.common.feign.fallback;

import com.iflytek.gulimall.common.exception.GulimallExceptinCodeEnum;
import com.iflytek.gulimall.common.feign.SeckillServiceAPI;
import com.iflytek.gulimall.common.feign.vo.SecSessionSkuVO;
import com.iflytek.gulimall.common.utils.ResultBody;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 此接口实现需要放在接口调用方的spring容器
 * 中
 */
@Component
public class SeckillServiceAPIFallBack implements SeckillServiceAPI {
    @Override
    public ResultBody<SecSessionSkuVO> getSecSessionSkuVOBySkuId(Long skuId) {
        return new ResultBody<>(GulimallExceptinCodeEnum.TOOMANG_REQUEST_EXCEPTION);
    }

    @Override
    public ResultBody<List<SecSessionSkuVO>> getCurrentSecSessionSkuVOS() {
        return new ResultBody<>(GulimallExceptinCodeEnum.TOOMANG_REQUEST_EXCEPTION);
    }
}
