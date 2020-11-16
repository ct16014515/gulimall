package com.iflytek.gulimall.member.service.impl;

import com.iflytek.gulimall.common.feign.vo.MemberReceiveAddressVO;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.gulimall.common.utils.PageUtils;
import com.iflytek.gulimall.common.utils.Query;

import com.iflytek.gulimall.member.dao.MemberReceiveAddressDao;
import com.iflytek.gulimall.member.entity.MemberReceiveAddressEntity;
import com.iflytek.gulimall.member.service.MemberReceiveAddressService;


@Service("memberReceiveAddressService")
public class MemberReceiveAddressServiceImpl extends ServiceImpl<MemberReceiveAddressDao, MemberReceiveAddressEntity> implements MemberReceiveAddressService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberReceiveAddressEntity> page = this.page(
                new Query<MemberReceiveAddressEntity>().getPage(params),
                new QueryWrapper<MemberReceiveAddressEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<MemberReceiveAddressVO> getMemberReceiveAddressByUid(Long uid) {
        List<MemberReceiveAddressEntity> memberReceiveAddressEntityList = this.baseMapper.getMemberReceiveAddressByUid(uid);
        List<MemberReceiveAddressVO> collect = memberReceiveAddressEntityList.stream().map(item -> {
            MemberReceiveAddressVO memberReceiveAddressVO = new MemberReceiveAddressVO();
            BeanUtils.copyProperties(item, memberReceiveAddressVO);
            return memberReceiveAddressVO;
        }).collect(Collectors.toList());


        return collect;
    }

}