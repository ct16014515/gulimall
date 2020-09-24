package com.iflytek.gulimall.product.dao;

import com.iflytek.gulimall.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.gulimall.product.vo.SkuAttrsValsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * spu信息
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 01:12:52
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {
    @Select("SELECT name from pms_brand where brand_id =#{brandId} limit 1")
    String selectbrandNameByBrandId(@Param("brandId") Long brandId);

    @Select("SELECT img_url FROM `pms_spu_images` WHERE spu_id=#{spuId} limit 1")
    String selectSpuImgBySpuId(@Param("spuId") Long spuId);

    @Select("SELECT attr_name attrName,attr_value attrValue FROM pms_sku_sale_attr_value  WHERE sku_id = #{skuId}")
    List<SkuAttrsValsVO> selectSkuAttrsValsVOBySkuId(@Param("skuId") Long skuId);
}
