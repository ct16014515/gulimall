package com.iflytek.gulimall.product.service.impl;

import com.iflytek.gulimall.product.entity.AttrEntity;
import com.iflytek.gulimall.product.entity.CategoryEntity;
import com.iflytek.gulimall.product.service.AttrService;
import com.iflytek.gulimall.product.service.CategoryService;
import com.iflytek.gulimall.product.vo.AttrGroupWithAttrsVo;
import com.iflytek.gulimall.product.vo.SpuItemAttrGroupVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.gulimall.common.utils.PageUtils;
import com.iflytek.gulimall.common.utils.Query;

import com.iflytek.gulimall.product.dao.AttrGroupDao;
import com.iflytek.gulimall.product.entity.AttrGroupEntity;
import com.iflytek.gulimall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long categoryId) {
        //判断是否带key
        String key = params.get("key").toString();
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        if (categoryId == 0) {
            if (!StringUtils.isEmpty(key)) {
                queryWrapper.eq("attr_group_id", key).or().like("attr_group_name", key);
            }
        } else {
            queryWrapper.eq("catelog_id", categoryId);
            if (!StringUtils.isEmpty(key)) {
                queryWrapper.and((obj) -> {
                    obj.eq("attr_group_id", key).or().like("attr_group_name", key);
                });
            }

        }
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 根据id查询AttrGroupEntity实体
     *
     * @param attrGroupId
     * @return
     */
    @Override
    public AttrGroupEntity getAttrGroupEntityById(Long attrGroupId) {
        AttrGroupEntity attrGroupEntity = getById(attrGroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        CategoryEntity categoryEntity = categoryService.getById(catelogId);
        List<Long> list = new ArrayList<>();
        //三级目录在最前面,[734,80,9]; 需要改为[9,80,734];
        List<Long> getcatelogIds = getcatelogIds(list, categoryEntity);
        //将排序反转
        Collections.reverse(getcatelogIds);
        attrGroupEntity.setCatelogIds(getcatelogIds);
        return attrGroupEntity;
    }


    @Override
    public List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId) {
        //1、查出当前spu对应的所有属性的分组信息以及当前分组下的所有属性对应的值
        AttrGroupDao baseMapper = this.getBaseMapper();
        List<SpuItemAttrGroupVo> vos = baseMapper.getAttrGroupWithAttrsBySpuId(spuId, catalogId);
        return vos;
    }

    @Override
    public List<AttrGroupEntity> getListByAttrGroupId(Long attrGroupId) {
        List<AttrGroupEntity> list = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("attr_group_id", attrGroupId));
        return list;
    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {

        //1、查询分组信息
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));

        //2、查询所有属性
        List<AttrGroupWithAttrsVo> collect = attrGroupEntities.stream().map(group -> {
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(group,attrGroupWithAttrsVo);

            List<AttrEntity> attrs = attrService.getRelationAttr(attrGroupWithAttrsVo.getAttrGroupId());
            attrGroupWithAttrsVo.setAttrs(attrs);

            return attrGroupWithAttrsVo;
        }).collect(Collectors.toList());

        return collect;
    }

    /**
     * 根据entity查找分类id,直到一级分类
     *
     * @param catelogIds
     * @param entity
     * @return
     */
    private List<Long> getcatelogIds(List<Long> catelogIds, CategoryEntity entity) {
        catelogIds.add(entity.getCatId());
        if (entity.getParentCid() != 0) {
            CategoryEntity categoryEntity = categoryService.getById(entity.getParentCid());
            getcatelogIds(catelogIds, categoryEntity);
        }
        return catelogIds;
    }


}