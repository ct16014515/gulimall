package com.iflytek.gulimall.member.service.impl;

import com.iflytek.common.utils.ResultBody;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.common.utils.PageUtils;
import com.iflytek.common.utils.Query;

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
    public ResultBody<List<MemberReceiveAddressEntity>> getMemberReceiveAddressByUid(Long uid) {
        List<MemberReceiveAddressEntity> memberReceiveAddressEntityList = this.baseMapper.getMemberReceiveAddressByUid(uid);
        return new ResultBody<>(memberReceiveAddressEntityList);
    }

}