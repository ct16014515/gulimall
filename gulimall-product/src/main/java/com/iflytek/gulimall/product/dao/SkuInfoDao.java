package com.iflytek.gulimall.product.dao;


import com.iflytek.gulimall.common.feign.vo.SkuInfoPriceVO;

import com.iflytek.gulimall.product.entity.SkuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * sku信息
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 01:12:52
 */
@Mapper
public interface SkuInfoDao extends BaseMapper<SkuInfoEntity> {
    @Select("SELECT  CONCAT(attr_name,':',attr_value) FROM pms_sku_sale_attr_value WHERE sku_id =#{skuId} ")
    List<String> getskuAttrsBySkuId(@Param("skuId") Long skuId);

    @Select("SELECT sku_id,price FROM pms_sku_info WHERE sku_id =#{skuId}")
    SkuInfoPriceVO getSkuPriceBySkuId(@Param("skuId") Long skuId);
}
