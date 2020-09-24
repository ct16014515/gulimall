package com.iflytek.gulimall.ware.dao;

import com.iflytek.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.gulimall.ware.vo.WareSkuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 商品库存
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 11:15:53
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
    @Select("SELECT IFNULL(SUM(stock-stock_locked) ,0) FROM wms_ware_sku WHERE sku_id=#{skuid} ")
    Long selectStockBySkuId(@Param("skuid") Long skuid);

    @Select("SELECT sum(stock-stock_locked-#{num}) skuRemainingQuantity,id,ware_id wareId FROM wms_ware_sku where sku_id =#{skuId} GROUP BY id")
    List<WareSkuVO> selectWareIdsBySkuId(@Param("skuId") Long skuId, @Param("num") Integer num);

    @Update("UPDATE wms_ware_sku SET stock_locked=stock_locked+#{num} where id =#{id}")
    void updateWareSkuStockLock(@Param("id") Long id, @Param("num") Integer num);
}
