package com.iflytek.gulimall.product.dao;

import com.iflytek.common.model.es.Attr;
import com.iflytek.gulimall.product.entity.ProductAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * spu属性值
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 01:12:52
 */
@Mapper
public interface ProductAttrValueDao extends BaseMapper<ProductAttrValueEntity> {
   @Select("SELECT" +
           " attr_id attrId, " +
           " attr_name attrName," +
           " value_select  valueSelect" +
           " FROM " +
           " pms_attr  " +
           "WHERE " +
           " attr_id IN ( " +
           "SELECT " +
           " tattr_id  " +
           "FROM " +
           "tpms_product_attr_value  " +
           "WHERE " +
           " spu_id = #{spuId})")
    List<Attr> selectAttrsBySpuId(@Param("spuId") Long spuId);
}
