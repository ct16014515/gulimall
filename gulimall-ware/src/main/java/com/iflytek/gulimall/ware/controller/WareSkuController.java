package com.iflytek.gulimall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.iflytek.gulimall.common.feign.WareServiceAPI;
import com.iflytek.gulimall.common.feign.vo.WareHasStockVO;
import com.iflytek.gulimall.common.feign.vo.WareSkuLockVO;
import com.iflytek.gulimall.common.utils.ResultBody;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.iflytek.gulimall.ware.entity.WareSkuEntity;
import com.iflytek.gulimall.ware.service.WareSkuService;
import com.iflytek.gulimall.common.utils.PageUtils;
import com.iflytek.gulimall.common.utils.R;


/**
 * 商品库存
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 11:15:53
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController implements WareServiceAPI {
    @Autowired
    private WareSkuService wareSkuService;

    @PostMapping("/hasStock")
    public R hasStock(@RequestBody List<Long> skuIds) {
        List<WareHasStockVO> list = wareSkuService.hasStock(skuIds);
        return R.ok().setData(list);

    }

    @GetMapping("/hasStock/{skuId}")
    public ResultBody<Integer> hasStockById(@PathVariable("skuId") Long skuId) {
        ResultBody<Integer> resultBody = wareSkuService.hasStock(skuId);
        return resultBody;
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("ware:waresku:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("ware:waresku:info")
    public R info(@PathVariable("id") Long id) {
        WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("ware:waresku:save")
    public R save(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("ware:waresku:update")
    public R update(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("ware:waresku:delete")
    public R delete(@RequestBody Long[] ids) {
        wareSkuService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    @PostMapping("/wareSkuLock")
    public ResultBody wareSkuLock(@RequestBody WareSkuLockVO wareSkuLockVO) {
        ResultBody resultBody = wareSkuService.wareSkuLock(wareSkuLockVO);
        return resultBody;
    }


}
