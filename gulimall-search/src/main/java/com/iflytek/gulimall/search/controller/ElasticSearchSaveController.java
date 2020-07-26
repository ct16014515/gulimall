package com.iflytek.gulimall.search.controller;

import com.iflytek.common.exception.GulimallExceptinCodeEnum;
import com.iflytek.common.model.es.SkuEsModel;
import com.iflytek.common.utils.ResultBody;
import com.iflytek.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequestMapping("/search/save")
@RestController
@Slf4j
public class ElasticSearchSaveController {

    @Autowired
    private ProductSaveService productSaveService;

    /**
     * 商品上架
     * @param skuEsModels
     * @return
     */
    @PostMapping("/product")
    public ResultBody productUp(@RequestBody List<SkuEsModel> skuEsModels)  {
        boolean b= false;
        try {
            b = productSaveService.productUp(skuEsModels);
        } catch (Exception e) {
            log.error("商品上架错误:{}",e);
        }
        if (b){
           return new ResultBody(0,"success",null);

        }else {
           return  new  ResultBody(GulimallExceptinCodeEnum.PRODUCT_UP_ERROR,null);
       }
    }


}
