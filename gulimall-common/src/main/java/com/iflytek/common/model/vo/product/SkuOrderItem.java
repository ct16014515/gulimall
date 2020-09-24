package com.iflytek.common.model.vo.product;

import lombok.Data;

import java.io.Serializable;
@Data
public class SkuOrderItem implements Serializable {
    private Long skuId;

    private String skuName;

    private Integer num;
}
