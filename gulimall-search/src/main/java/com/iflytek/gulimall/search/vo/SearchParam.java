package com.iflytek.gulimall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * 构造查询条件,
 * ?catalog3Id=167&keyword=手机&sort=saleCount_desc
 */
@Data
public class SearchParam {

    private String keyword;
    private Long catalog3Id;
    /**
     * 页面排序条件
     * saleCount_desc/asc
     * skuPrice_desc/asc
     * hotScore_desc/asc
     */
    private String sort;
    private Integer hasStock=1;//是否有货 0没货 1有货
    /**
     * 价格区间
     * 200_500  _500  500_ 三种
     */
    private String skuPrice;//价格区间

    /**
     * 品牌id多选
     * &brandId=1&brandId=2 可以直接用list<Long>接收
     */
    private List<Long> brandId;

    /**
     * 属性
     * &attrs=1_5寸:6寸&attrs=2_安卓:ios
     */
    private List<String> attrs;
    /**
     * 当前页
     */
    private Long pageNumber=1L;
    /**
     * 每页大小
     */
    private Long pageSize=12L;


}
