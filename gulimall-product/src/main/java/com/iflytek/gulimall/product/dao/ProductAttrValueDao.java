package com.iflytek.gulimall.product.dao;

import com.iflytek.common.model.es.Attrs;
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
   @Select("SELECT\n" +
           "\tt1.attr_id attrId,\n" +
           "\tt1.attr_name attrName,\n" +
           "\tt1.attr_value attrValue \n" +
           "FROM\n" +
           "\tpms_product_attr_value t1,\n" +
           "\tpms_attr t2\n" +
           "\tWHERE \n" +
           "\tt1.attr_id=t2.attr_id \n" +
           "\tAND t2.search_type=1 AND t1.spu_id=#{spuId}\n" +
           "\t")
    List<Attrs> selectAttrsBySpuId(@Param("spuId") Long spuId);
}
