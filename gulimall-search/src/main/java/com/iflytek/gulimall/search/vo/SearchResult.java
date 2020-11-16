package com.iflytek.gulimall.search.vo;

import com.iflytek.gulimall.common.model.es.SkuEsModel;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 搜索结果
 */
@Data
@ToString
public class SearchResult {
    /**
     * 查询的商品
     */
    private List<SkuEsModel> products;
    /**
     * 当前页
     */
    private Long pageNum;
    /**
     * 总页码
     */
    private Long totalPage;

    /**
     * 总记录数
     */
    private Long total;
    /**
     * 品牌 资源聚合
     */
    private List<BrandVO> brandVOS;
    /**
     * 分类 资源聚合
     */
    private List<CategoryVO> categoryVOS;

    /**
     * 属性
     */
    private List<AttrVO> attrVOS;







}
