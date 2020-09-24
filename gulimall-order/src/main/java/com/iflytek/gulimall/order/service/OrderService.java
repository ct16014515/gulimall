package com.iflytek.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iflytek.common.model.vo.order.OrderEntityVO;
import com.iflytek.common.utils.PageUtils;
import com.iflytek.common.utils.ResultBody;
import com.iflytek.gulimall.order.entity.OrderEntity;
import com.iflytek.gulimall.order.vo.OrderConfirmVO;
import com.iflytek.gulimall.order.vo.OrderSubmitResposeVO;
import com.iflytek.gulimall.order.vo.OrderSubmitVO;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 00:28:50
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVO toTrade() throws ExecutionException, InterruptedException;

    BigDecimal getFreightMoneyByAddressId(Long addressId);

    OrderSubmitResposeVO submitOrder(OrderSubmitVO orderSubmitVO);

    OrderEntity getOrderEntityByOrderIdAndUserId(Long orderId, Long userId);

    ResultBody<OrderEntity> getOrderEntityByOrderSn(String orderSn);

    void closeOrder(OrderEntity entity);
}

