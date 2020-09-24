package com.iflytek.gulimall.cart.feign;

import com.iflytek.common.model.vo.product.SkuInfoVO;
import com.iflytek.common.utils.R;
import com.iflytek.common.utils.ResultBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "gulimall-product")
public interface ProductService {

    @RequestMapping("/product/skuinfo/info/{skuId}")
    public ResultBody<SkuInfoVO> info(@PathVariable("skuId") Long skuId);

    @RequestMapping("/product/skuinfo/getskuAttrsBySkuId")
    public ResultBody<List<String>> getskuAttrsBySkuId(@RequestParam("skuId") Long skuId);
}
