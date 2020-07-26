package com.iflytek.gulimall.product.dao;

import com.iflytek.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 商品三级分类
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 01:12:52
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
   @Select("SELECT name from pms_category WHERE cat_id=#{catalogId}")
    String selectNameById(@Param("catalogId") Long catalogId);
}
