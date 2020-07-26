package com.iflytek.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iflytek.common.utils.PageUtils;
import com.iflytek.gulimall.coupon.entity.CouponEntity;

import java.util.Map;

/**
 * 优惠券信息
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 11:03:00
 */
public interface CouponService extends IService<CouponEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

