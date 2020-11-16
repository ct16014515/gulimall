package com.iflytek.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.iflytek.gulimall.common.feign.vo.Auth2SocialVO;
import com.iflytek.gulimall.common.feign.vo.MemberVO;
import com.iflytek.gulimall.common.feign.vo.RegisterVO;
import com.iflytek.gulimall.common.feign.vo.UserLoginVO;
import com.iflytek.gulimall.common.utils.PageUtils;
import com.iflytek.gulimall.common.utils.ResultBody;
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

    ResultBody register(RegisterVO registerVO);


    ResultBody<MemberVO> login(UserLoginVO userLoginVO);

    ResultBody<MemberVO> auth2Login(Auth2SocialVO auth2SocialVO);
}

