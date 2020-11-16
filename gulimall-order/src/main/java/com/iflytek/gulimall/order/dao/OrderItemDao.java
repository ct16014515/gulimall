package com.iflytek.gulimall.order.dao;


import com.iflytek.gulimall.common.feign.vo.OrderItemVO;

import com.iflytek.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 订单项信息
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 00:28:50
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
    @Select("SELECT sku_name  FROM `oms_order_item` WHERE order_sn=#{orderSn} limit 1")
    String getSkuNameByOrderSn(@Param("orderSn") String orderSn);

    @Select("SELECT sku_pic skuPic,sku_id skuId,sku_name skuName,sku_quantity skuCount,sku_attrs_vals skuAttrsVals FROM oms_order_item WHERE order_sn =#{orderSn} ")
    List<OrderItemVO> selectOrderItemVOListByOrderSn(@Param("orderSn") String orderSn);
}
