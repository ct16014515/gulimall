package com.iflytek.gulimall.product.service.impl;

import com.iflytek.gulimall.product.vo.SkuItemSaleAttrVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.common.utils.PageUtils;
import com.iflytek.common.utils.Query;

import com.iflytek.gulimall.product.dao.SkuSaleAttrValueDao;
import com.iflytek.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.iflytek.gulimall.product.service.SkuSaleAttrValueService;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuItemSaleAttrVo> getSaleAttrBySpuId(Long spuId) {
        SkuSaleAttrValueDao baseMapper = this.getBaseMapper();
        List<SkuItemSaleAttrVo> saleAttrVos = baseMapper.getSaleAttrBySpuId(spuId);
        return saleAttrVos;
    }

}