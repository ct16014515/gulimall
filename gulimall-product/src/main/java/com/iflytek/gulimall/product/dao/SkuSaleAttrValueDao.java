package com.iflytek.gulimall.product.dao;

import com.iflytek.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.gulimall.product.vo.SkuItemSaleAttrVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 01:12:52
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    @Select("SELECT\n" +
            "\tssav.attr_id attr_id,\n" +
            "\tssav.attr_name attr_name,\n" +
            "\tssav.attr_value,\n" +
            "\tgroup_concat( DISTINCT info.sku_id ) sku_ids \n" +
            "FROM\n" +
            "\t pms_sku_info info\n" +
            "\t LEFT JOIN pms_sku_sale_attr_value ssav ON ssav.sku_id = info.sku_id \n" +
            "WHERE\n" +
            "\t info.spu_id = #{spuId}\n" +
            "\t\n" +
            "GROUP BY\n" +
            "\tssav.attr_id,\n" +
            "\tssav.attr_name,\n" +
            "\tssav.attr_value")
    List<SkuItemSaleAttrVo> getSaleAttrBySpuId(@Param("spuId") Long spuId);
}
