package com.iflytek.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.gulimall.common.utils.PageUtils;
import com.iflytek.gulimall.common.utils.Query;

import com.iflytek.gulimall.product.dao.BrandDao;
import com.iflytek.gulimall.product.entity.BrandEntity;
import com.iflytek.gulimall.product.service.BrandService;
import org.springframework.util.StringUtils;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Object key = params.get("key");
        IPage<BrandEntity> page = null;
        if (StringUtils.isEmpty(key)) {
            page = this.page(
                    new Query<BrandEntity>().getPage(params),
                    new QueryWrapper<BrandEntity>()
            );
        } else {
            //如果有关键字,根据关键字精确查询brand_id 模糊匹配 name descript
            page = this.page(
                    new Query<BrandEntity>().getPage(params),
                    new QueryWrapper<BrandEntity>().
                            eq("brand_id",  key.toString()).
                            or().like("name", key).or().like("descript",key)
            );
        }
        return new PageUtils(page);
    }

}