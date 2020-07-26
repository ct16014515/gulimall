package com.iflytek.gulimall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.iflytek.common.exception.GulimallExceptinCodeEnum;
import com.iflytek.common.exception.RRException;
import com.iflytek.common.model.es.Attrs;
import com.iflytek.common.model.es.SkuEsModel;
import com.iflytek.common.model.vo.WareHasStockVo;

import com.iflytek.common.utils.ResultBody;
import com.iflytek.gulimall.product.dao.*;
import com.iflytek.gulimall.product.entity.*;
import com.iflytek.gulimall.product.feign.SerachService;
import com.iflytek.gulimall.product.feign.WareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.common.utils.PageUtils;
import com.iflytek.common.utils.Query;

import com.iflytek.gulimall.product.service.SpuInfoService;


@Service("spuInfoService")
@Slf4j
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    SkuInfoDao skuInfoDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    WareService wareService;
    @Autowired
    ProductAttrValueDao productAttrValueDao;
    @Autowired
    BrandDao brandDao;
    @Autowired
    SerachService serachService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 商品上架
     *
     * @param spuId 1、将属性改为已上架，
     *              2、构建SkuEsModel模型,再远程调用搜索服务,向搜索服务索引记录
     */
    @Override
    public void up(Long spuId) {

        List<SkuInfoEntity> skuList = skuInfoDao.selectList(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        List<Long> skuIdList = skuList.stream().map(item -> {
            return item.getSkuId();
        }).collect(Collectors.toList());
        //TODO 1、hasStock是否有库存 远程调用库存服务查询是否有库存
        Map<Long, Boolean> stockVosMap = null;
        try {
            TypeReference<List<WareHasStockVo>> typeReference = new TypeReference<List<WareHasStockVo>>() {
            };
            List<WareHasStockVo> stockVos = wareService.hasStock(skuIdList).getData(typeReference);
            stockVosMap = stockVos.stream().collect(Collectors.toMap(WareHasStockVo::getSkuId, item -> item.getHasStock()));
        } catch (Exception e) {
            log.info("库存服务查询异常:{}", e);
        }
        //TODO 4、查询当前sku用来被检索的属性规格
        List<Attrs> attrs = productAttrValueDao.selectAttrsBySpuId(spuId);
        Map<Long, Boolean> finalStockVosMap = stockVosMap;
        List<SkuEsModel> esModels = skuList.stream().map(skuInfoEntity -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(skuInfoEntity, skuEsModel);
            skuEsModel.setSkuPrice(skuInfoEntity.getPrice());//价格
            skuEsModel.setSkuImg(skuInfoEntity.getSkuDefaultImg());//图片
            //是否有库存,如果远程调用失败默认为有库存
            skuEsModel.setHasStock(finalStockVosMap == null ? true : finalStockVosMap.get(skuInfoEntity.getSkuId()));//是否头库存
            //TODO 2、 hotScore 热度评分 0
            skuEsModel.setHotScore(0L);
            //TODO 3、查询品牌和分类信息
            //品牌名称 分类名称
            BrandEntity brandEntity = brandDao.selectById(skuInfoEntity.getBrandId());
            skuEsModel.setBrandName(brandEntity.getName());
            skuEsModel.setBrandImg(brandEntity.getLogo());
            String categoryName = categoryDao.selectNameById(skuInfoEntity.getCatalogId());
            skuEsModel.setCatalogName(categoryName);
            //当前sku用来被检索的属性
            skuEsModel.setAttrs(attrs);
            return skuEsModel;
        }).collect(Collectors.toList());
        ResultBody resultBody = serachService.productUp(esModels);
        if (0 == resultBody.getCode()) {
            SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
            spuInfoEntity.setId(spuId);
            spuInfoEntity.setPublishStatus(1);
            spuInfoEntity.setUpdateTime(new Date());
            this.updateById(spuInfoEntity);
        } else {
            throw new RRException(GulimallExceptinCodeEnum.PRODUCT_UP_ERROR);
        }
    }


}