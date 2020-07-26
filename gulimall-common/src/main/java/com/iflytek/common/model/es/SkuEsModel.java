package com.iflytek.common.model.es;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
@Data
@ToString
public class SkuEsModel {
    private Long skuId;

    private Long spuId;

    private String skuTitle;//sku标题

    private BigDecimal skuPrice;//sku的价格

    private String skuImg;//sku的图片

    private Long saleCount;//销量

    private Boolean hasStock;//是否有库存

    private Long hotScore;//热度评分

    private Long brandId;//品牌id

    private Long catalogId;//分类id

    private String brandName;//品牌名称

    private String brandImg;//品牌照片

    private String catalogName;//分类名称

    private List<Attrs> attrs;

}
