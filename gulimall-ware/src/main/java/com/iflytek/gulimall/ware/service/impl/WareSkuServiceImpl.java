package com.iflytek.gulimall.ware.service.impl;


import com.iflytek.common.model.vo.product.WareHasStockVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.common.utils.PageUtils;
import com.iflytek.common.utils.Query;

import com.iflytek.gulimall.ware.dao.WareSkuDao;
import com.iflytek.gulimall.ware.entity.WareSkuEntity;
import com.iflytek.gulimall.ware.service.WareSkuService;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {


   @Autowired
   private WareSkuDao wareSkuDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 根据skuid判断是否有库存
     * @param skuIds
     * @return
     */
    @Override
    public List<WareHasStockVO> hasStock(List<Long> skuIds) {
        List<WareHasStockVO> list = skuIds.stream().map(skuid -> {
            WareHasStockVO wareHasStockVo = new WareHasStockVO();
            Long sumStock= wareSkuDao.selectStockBySkuId(skuid)==null?0:wareSkuDao.selectStockBySkuId(skuid);
            wareHasStockVo.setSkuId(skuid);
            wareHasStockVo.setHasStock(sumStock>0?1:0);
            return wareHasStockVo;
        }).collect(Collectors.toList());
        return list;
    }

}