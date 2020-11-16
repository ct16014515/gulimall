package com.iflytek.gulimall.common.feign.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class SkuOrderItem implements Serializable {
    private Long skuId;

    private String skuName;

    private Integer num;
}
