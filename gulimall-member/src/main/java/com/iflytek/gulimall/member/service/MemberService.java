package com.iflytek.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iflytek.common.utils.PageUtils;
import com.iflytek.gulimall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 11:08:00
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

