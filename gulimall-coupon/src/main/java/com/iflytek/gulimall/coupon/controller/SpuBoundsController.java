package com.iflytek.gulimall.coupon.controller;

import java.util.Arrays;
import java.util.Map;


import com.iflytek.gulimall.common.feign.CouponServiceAPI;
import com.iflytek.gulimall.common.feign.vo.SkuReductionVO;
import com.iflytek.gulimall.common.feign.vo.SpuBoundVO;
import com.iflytek.gulimall.common.utils.ResultBody;
import com.iflytek.gulimall.coupon.service.SkuFullReductionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.iflytek.gulimall.coupon.entity.SpuBoundsEntity;
import com.iflytek.gulimall.coupon.service.SpuBoundsService;
import com.iflytek.gulimall.common.utils.PageUtils;
import com.iflytek.gulimall.common.utils.R;



/**
 * 商品spu积分设置
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-10-19 16:40:01
 */
@RestController
@RequestMapping("coupon/spubounds")
public class SpuBoundsController implements CouponServiceAPI {
    @Autowired
    private SpuBoundsService spuBoundsService;



    @Autowired
    private SkuFullReductionService skuFullReductionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("coupon:spubounds:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuBoundsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("coupon:spubounds:info")
    public R info(@PathVariable("id") Long id){
		SpuBoundsEntity spuBounds = spuBoundsService.getById(id);

        return R.ok().put("spuBounds", spuBounds);
    }

//    /**
//     * 保存
//     */
//    @RequestMapping("/save")
//   // @RequiresPermissions("coupon:spubounds:save")
//    public R save(@RequestBody SpuBoundsEntity spuBounds){
//		spuBoundsService.save(spuBounds);
//
//        return R.ok();
//    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("coupon:spubounds:update")
    public R update(@RequestBody SpuBoundsEntity spuBounds){
		spuBoundsService.updateById(spuBounds);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("coupon:spubounds:delete")
    public R delete(@RequestBody Long[] ids){
		spuBoundsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }




    @Override
    @PostMapping("/save")
    public void saveSpuBounds(@RequestBody SpuBoundVO spuBoundVO) {
        SpuBoundsEntity spuBounds =new SpuBoundsEntity();
        BeanUtils.copyProperties(spuBoundVO,spuBounds);
        spuBoundsService.save(spuBounds);
    }

    @Override
    @PostMapping("/saveSkuFullReduction")
    public void saveSkuReduction(@RequestBody SkuReductionVO skuReductionVO) {
        skuFullReductionService.saveSkuReduction(skuReductionVO);
    }
}
