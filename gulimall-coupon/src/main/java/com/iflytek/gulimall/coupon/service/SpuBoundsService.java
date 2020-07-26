package com.iflytek.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iflytek.common.utils.PageUtils;
import com.iflytek.gulimall.coupon.entity.SpuBoundsEntity;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 11:02:59
 */
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

