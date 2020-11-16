package com.iflytek.gulimall.product.app;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.iflytek.gulimall.common.feign.ProductServiceAPI;
import com.iflytek.gulimall.common.feign.vo.OrderItemVO;
import com.iflytek.gulimall.common.feign.vo.SkuInfoPriceVO;
import com.iflytek.gulimall.common.feign.vo.SkuInfoVO;
import com.iflytek.gulimall.common.utils.ResultBody;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.iflytek.gulimall.product.entity.SkuInfoEntity;
import com.iflytek.gulimall.product.service.SkuInfoService;
import com.iflytek.gulimall.common.utils.PageUtils;
import com.iflytek.gulimall.common.utils.R;


/**
 * sku信息
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 01:12:52
 */
@RestController
@RequestMapping("product/skuinfo")
public class SkuInfoController implements ProductServiceAPI {
    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("product:skuinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = skuInfoService.queryPage(params);
        return R.ok().put("page", page);
    }


    @PostMapping("/list")
    public ResultBody<List<SkuInfoVO>> list(@RequestBody List<Long> skuIds) {
        List<SkuInfoVO> collect = skuInfoService.listByIds(skuIds).stream().map(
                item -> {
                    SkuInfoVO skuInfoVO = new SkuInfoVO();
                    BeanUtils.copyProperties(item, skuInfoVO);
                    return skuInfoVO;
                }
        ).collect(Collectors.toList());
        return new ResultBody<>(collect);
    }



    /**
     * 信息
     */
    @RequestMapping("/info/{skuId}")
    // @RequiresPermissions("product:skuinfo:info")
    public ResultBody info(@PathVariable("skuId") Long skuId) {
        SkuInfoEntity skuInfo = skuInfoService.getById(skuId);
        return new ResultBody<>(skuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:skuinfo:save")
    public R save(@RequestBody SkuInfoEntity skuInfo) {
        skuInfoService.save(skuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:skuinfo:update")
    public R update(@RequestBody SkuInfoEntity skuInfo) {
        skuInfoService.updateById(skuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:skuinfo:delete")
    public R delete(@RequestBody Long[] skuIds) {
        skuInfoService.removeByIds(Arrays.asList(skuIds));

        return R.ok();
    }

    /**
     * 获取销售属性
     *
     * @param skuId
     * @return
     */
    @GetMapping("/getskuAttrsBySkuId")
    // @RequiresPermissions("product:skuinfo:delete")
    public ResultBody<List<String>> getskuAttrsBySkuId(@RequestParam("skuId") Long skuId) {
        ResultBody<List<String>> resultBody = skuInfoService.getskuAttrsBySkuId(skuId);
        return resultBody;
    }

    @PostMapping("/getSkuPriceBySkuIds")
    public ResultBody<List<SkuInfoPriceVO>> getSkuPriceBySkuIds(@RequestBody List<Long> skuIds) {
        ResultBody<List<SkuInfoPriceVO>> resultBody = skuInfoService.getSkuPriceBySkuIds(skuIds);
        return resultBody;
    }

    @PostMapping("/getOrderItemsBySkuIds")
    public List<OrderItemVO> getOrderItemsBySkuIds(@RequestBody List<Long> skuIds) {
        List<OrderItemVO> orderItemVOS = skuInfoService.getOrderItemsBySkuIds(skuIds);
        return orderItemVOS;
    }


}
