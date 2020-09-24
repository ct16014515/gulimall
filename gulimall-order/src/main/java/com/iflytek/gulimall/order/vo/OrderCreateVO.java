package com.iflytek.gulimall.order.vo;

import com.iflytek.gulimall.order.entity.OrderEntity;
import com.iflytek.gulimall.order.entity.OrderItemEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderCreateVO implements Serializable {

    private OrderEntity orderEntity;

    private List<OrderItemEntity> orderItemEntities;

}
