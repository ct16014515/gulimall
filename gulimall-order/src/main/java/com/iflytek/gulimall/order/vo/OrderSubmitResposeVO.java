package com.iflytek.gulimall.order.vo;

import com.iflytek.gulimall.order.entity.OrderEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderSubmitResposeVO implements Serializable {

    private OrderEntity orderEntity;

    private Integer code; //0表示成功.1表示失败
}
