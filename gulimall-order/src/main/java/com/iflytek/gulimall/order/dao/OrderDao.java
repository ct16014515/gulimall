package com.iflytek.gulimall.order.dao;

import com.iflytek.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 00:28:50
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
