package com.iflytek.gulimall.coupon.dao;

import com.iflytek.gulimall.coupon.entity.CouponHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 * 
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-10-19 16:40:01
 */
@Mapper
public interface CouponHistoryDao extends BaseMapper<CouponHistoryEntity> {
	
}
