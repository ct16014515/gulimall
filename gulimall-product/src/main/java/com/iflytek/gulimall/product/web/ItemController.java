package com.iflytek.gulimall.product.web;

import com.iflytek.gulimall.product.service.SkuInfoService;
import com.iflytek.gulimall.product.service.SpuInfoService;
import com.iflytek.gulimall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

@Controller
public class ItemController {



    @Autowired
    SkuInfoService skuInfoService;
    /**
     * 商品详情
     * @param skuId
     * @param model
     * @return
     */
    @GetMapping("/{skuId}.html")
    public String getSkuItem(@PathVariable("skuId")Long skuId,Model model) throws ExecutionException, InterruptedException {

        SkuItemVo  skuItemVo=skuInfoService.getSkuItem(skuId);
        model.addAttribute("SkuItemVo",skuItemVo);
        System.out.println("开始查询商品详情:"+skuId);
        return "item";
    }

}