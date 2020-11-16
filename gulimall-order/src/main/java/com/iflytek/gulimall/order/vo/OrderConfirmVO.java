package com.iflytek.gulimall.order.vo;



import com.iflytek.gulimall.common.feign.vo.CartItemVO;
import com.iflytek.gulimall.common.feign.vo.MemberReceiveAddressVO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderConfirmVO implements Serializable {

    /**
     * 收货地址
     */
    private List<MemberReceiveAddressVO> memberReceiveAddressVOS;
    /**
     * 购物列表
     */
    private List<CartItemVO> orderItemVOS;


    /**
     * 积分
     */
    private Integer integration;


    private BigDecimal totalMoney; //总商品金额

    private BigDecimal payMoney;//应付总额

    private BigDecimal freightMoney = new BigDecimal("0.00");//运费
    private BigDecimal returnMoney = new BigDecimal("0.00");//返现
    private Integer orderItemCount;//购物件数量
    private String orderToken;//防重复令牌


}
