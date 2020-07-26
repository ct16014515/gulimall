package com.iflytek.gulimall.product.feign;

import com.iflytek.common.model.es.SkuEsModel;
import com.iflytek.common.utils.ResultBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimall-search")
public interface SerachService {
    @PostMapping("search/save/product")
    public ResultBody productUp(@RequestBody List<SkuEsModel> skuEsModels);


}
