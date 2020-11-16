package com.iflytek.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iflytek.gulimall.common.model.mq.to.OrderEntityReleaseTO;
import com.iflytek.gulimall.common.model.mq.to.SecKillOrderCreateTO;
import com.iflytek.gulimall.common.utils.PageUtils;
import com.iflytek.gulimall.order.entity.OrderEntity;
import com.iflytek.gulimall.order.vo.OrderConfirmVO;
import com.iflytek.gulimall.order.vo.OrderSubmitResposeVO;
import com.iflytek.gulimall.order.vo.OrderSubmitVO;
import com.iflytek.gulimall.order.vo.PayVO;
import com.lly835.bestpay.model.PayResponse;

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

    OrderEntity getOrderEntityByOrderSnAndUserId(String orderSn, Long userId);

    OrderEntity getOrderEntityByOrderSn(String orderSn);

    void closeOrder(OrderEntityReleaseTO entityTO);

    String getSkuNameByOrderSn(String orderSn);

    PayVO getPayVOByOrderSn(String orderSn);

    PageUtils orderWithOrderItemList(Map<String, Object> params);

    String aliPayCallBack(Map<String, String> stringMap);

    String wxPayCallBack(PayResponse payResponse);


    void updatePayTypeByOrderId(Long orderId, Integer payType);

    void createSecKillOrder(SecKillOrderCreateTO entityTO);
}

