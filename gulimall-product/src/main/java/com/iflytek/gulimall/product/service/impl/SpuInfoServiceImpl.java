package com.iflytek.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.iflytek.gulimall.common.exception.GulimallExceptinCodeEnum;
import com.iflytek.gulimall.common.exception.RRException;
import com.iflytek.gulimall.common.feign.SerachServiceAPI;
import com.iflytek.gulimall.common.feign.WareServiceAPI;
import com.iflytek.gulimall.common.feign.vo.SkuEsModel;
import com.iflytek.gulimall.common.feign.vo.WareHasStockVO;
import com.iflytek.gulimall.common.model.es.Attr;



import com.iflytek.gulimall.common.utils.ResultBody;

import com.iflytek.gulimall.product.dao.*;
import com.iflytek.gulimall.product.entity.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.gulimall.common.utils.PageUtils;
import com.iflytek.gulimall.common.utils.Query;

import com.iflytek.gulimall.product.service.SpuInfoService;


@Service("spuInfoService")
@Slf4j
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    SkuInfoDao skuInfoDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    WareServiceAPI wareServiceAPI;
    @Autowired
    ProductAttrValueDao productAttrValueDao;
    @Autowired
    BrandDao brandDao;
    @Autowired
    SerachServiceAPI serachServiceAPI;

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
        Map<Long, Integer> stockVosMap = null;
        try {
            TypeReference<List<WareHasStockVO>> typeReference = new TypeReference<List<WareHasStockVO>>() {
            };
            List<WareHasStockVO> stockVos = wareServiceAPI.hasStock(skuIdList).getData(typeReference);
            stockVosMap = stockVos.stream().collect(Collectors.toMap(WareHasStockVO::getSkuId, item -> item.getHasStock()));
        } catch (Exception e) {
            log.info("库存服务查询异常:{}", e);
        }
        //TODO 4、查询当前sku用来被检索的属性规格
        List<Attr> attrs = productAttrValueDao.selectAttrsBySpuId(spuId);
        List<Attr> attrList =new ArrayList<>();
        attrs.stream().forEach(a->{
            String valueSelect = a.getValueSelect();
            List<String> strings = JSON.parseObject(valueSelect, new TypeReference<List<String>>() {
            });
            strings.stream().forEach(s->{
                Attr attr1 =new Attr();
                attr1.setAttrValue(s);
                attr1.setAttrName(a.getAttrName());
                attr1.setAttrId(a.getAttrId());
                attrList.add(attr1);
            });
        });
        Map<Long, Integer> finalStockVosMap = stockVosMap;
        List<SkuEsModel> esModels = skuList.stream().map(skuInfoEntity -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(skuInfoEntity, skuEsModel);
            skuEsModel.setSkuPrice(skuInfoEntity.getPrice());//价格
            skuEsModel.setSkuImg(skuInfoEntity.getSkuDefaultImg());//图片
            //是否有库存,如果远程调用失败默认为有库存
            skuEsModel.setHasStock(finalStockVosMap == null ? 0 : finalStockVosMap.get(skuInfoEntity.getSkuId()));//是否头库存
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
            skuEsModel.setAttrs(attrList);
            return skuEsModel;
        }).collect(Collectors.toList());
        ResultBody resultBody = serachServiceAPI.productUp(esModels);
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


    public static void main(String[] args) {
        String str="[\"3000mAh\"]";

//        List<String>list=new ArrayList<>();
//        list.add("3000mAh");
//        list.add("4000mAh");
//        list.add("5000mAh");
//        String s = JSON.toJSONString(list);
//        System.out.println(s);
        List<String> strings = JSON.parseObject(str, new TypeReference<List<String>>() {
        });
        System.out.println(strings);


    }
}