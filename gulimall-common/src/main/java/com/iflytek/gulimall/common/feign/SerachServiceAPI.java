package com.iflytek.gulimall.common.feign;

import com.iflytek.gulimall.common.feign.vo.SkuEsModel;

import com.iflytek.gulimall.common.utils.ResultBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimall-search")
public interface SerachServiceAPI {
    @PostMapping("search/save/product")
    public ResultBody productUp(@RequestBody List<SkuEsModel> skuEsModels);


}
