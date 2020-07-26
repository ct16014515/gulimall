package com.iflytek.gulimall.order.dao;

import com.iflytek.gulimall.order.entity.OrderSettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单配置信息
 * 
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 00:28:49
 */
@Mapper
public interface OrderSettingDao extends BaseMapper<OrderSettingEntity> {
	
}
