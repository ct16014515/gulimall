package com.iflytek.gulimall.cart.vo;

import com.iflytek.common.model.vo.cart.CartItemVO;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@ToString
public class CartVO implements Serializable {
    private List<CartItemVO> cartItemVOS;
    private BigDecimal totalMoney;//总费用
    private Integer totalCount;//总数量
    private BigDecimal reduceMoney=new BigDecimal("0.00");//减免的费用默认是0

}
