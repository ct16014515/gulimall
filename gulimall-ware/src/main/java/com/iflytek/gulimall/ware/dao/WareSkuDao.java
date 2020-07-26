package com.iflytek.gulimall.ware.dao;

import com.iflytek.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 商品库存
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 11:15:53
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
     @Select("SELECT SUM(stock-stock_locked) FROM wms_ware_sku WHERE sku_id=#{skuid} ")
    Long selectStockBySkuId(@Param("skuid") Long skuid);
}
