package com.iflytek.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iflytek.common.utils.PageUtils;
import com.iflytek.gulimall.coupon.entity.MemberPriceEntity;

import java.util.Map;

/**
 * 商品会员价格
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 11:02:59
 */
public interface MemberPriceService extends IService<MemberPriceEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

