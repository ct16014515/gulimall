package com.iflytek.common.model.vo.cart;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@ToString
public class CartItemVO implements Serializable {

    private String skuTitle;//商品标题
    private List<String> skuAttrs;//商品属性
    private String skuImg;//商品图片
    private BigDecimal skuPrice;//商品单价
    private Integer skuCount;//商品数量
    private BigDecimal skuTotalMoney;//sku总价
    private Long skuId;
    /**
     * 0:选中 1:未选中
     */
    private Integer isChecked=0;//是否选中
    /**
     * 0表示没货,1表示有货
     */
    private Integer hasStock=1;

    private BigDecimal weight;
}
