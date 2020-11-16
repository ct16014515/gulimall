package com.iflytek.gulimall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iflytek.gulimall.common.exception.GulimallExceptinCodeEnum;

import com.iflytek.gulimall.common.feign.vo.Auth2SocialVO;
import com.iflytek.gulimall.common.feign.vo.MemberVO;
import com.iflytek.gulimall.common.feign.vo.RegisterVO;
import com.iflytek.gulimall.common.feign.vo.UserLoginVO;
import com.iflytek.gulimall.common.utils.HttpUtils;
import com.iflytek.gulimall.common.utils.PageUtils;
import com.iflytek.gulimall.common.utils.Query;
import com.iflytek.gulimall.common.utils.ResultBody;
import com.iflytek.gulimall.member.dao.MemberLevelDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.iflytek.gulimall.member.dao.MemberDao;
import com.iflytek.gulimall.member.entity.MemberEntity;
import com.iflytek.gulimall.member.service.MemberService;
import org.springframework.transaction.annotation.Transactional;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    MemberLevelDao memberLevelDao;
    @Autowired
    MemberDao memberDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }


    /**
     * 用户注册
     *
     * @param
     * @return
     */
    @Override
    @Transactional
    public ResultBody register(RegisterVO registerVO) {
        //查询此手机号和姓名是否已经存在
        Integer phoneCount = memberDao.selectCountByPhone(registerVO.getPhone());
        if (phoneCount > 0) {
            return new ResultBody(GulimallExceptinCodeEnum.MEMBER_PHONE_EXIT_ERROR, null);
        }
        //查询此手机号和姓名是否已经存在
        Integer usernameCount = memberDao.selectCountByUsername(registerVO.getUsername());
        if (usernameCount > 0) {
            return new ResultBody(GulimallExceptinCodeEnum.MEMBER_USERNAME_EXIT_ERROR, null);
        }
        //查询用户等级
        Long levelId = memberLevelDao.selectDefalutLevelId();
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setLevelId(levelId);
        String password = registerVO.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePassword = bCryptPasswordEncoder.encode(password);
        memberEntity.setPassword(encodePassword);
        memberEntity.setMobile(registerVO.getPhone());
        memberEntity.setUsername(registerVO.getUsername());
        String nickName = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        memberEntity.setNickname("gl_" + nickName);
        memberEntity.setIntegration(0);
        this.save(memberEntity);
        return new ResultBody();
    }

    /**
     * 用户登录
     * 先根据姓名查查不到根据手机号码查
     *
     * @param userLoginVO
     * @return
     */
    @Override
    public ResultBody<MemberVO> login(UserLoginVO userLoginVO) {
        MemberEntity memberEntity = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("username", userLoginVO.getAccount()));
        if (memberEntity == null) {
            MemberEntity phoneMemberEntity = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("mobile", userLoginVO.getAccount()));
            if (phoneMemberEntity == null) {
                return new ResultBody(GulimallExceptinCodeEnum.MEMBER_ACCOUNT_NOTEXIT_ERROR);
            } else {
                memberEntity = phoneMemberEntity;
            }
        }
        String password = memberEntity.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches(userLoginVO.getPassword(), password);
        if (!matches) {
            return new ResultBody(GulimallExceptinCodeEnum.MEMBER_PASSWORD_ERROR);
        }
        MemberVO memberVO = new MemberVO();
        memberVO.setUserId(memberEntity.getId());
        memberVO.setNickname(memberEntity.getNickname());
        memberVO.setIntegration(memberEntity.getIntegration() == null ? 0 : memberEntity.getIntegration());
        return new ResultBody(memberVO);
    }

    @Override
    public ResultBody<MemberVO> auth2Login(Auth2SocialVO auth2SocialVO) {
        MemberEntity memberEntity = baseMapper.selectOne(new QueryWrapper<MemberEntity>().
                eq("source_type", auth2SocialVO.getType()).
                eq("social_uid", auth2SocialVO.getUid()).last("limit 1"));
        if (auth2SocialVO.getType() == 1) {
            //如果是微博
            return getWeiboMemberVOResultBody(auth2SocialVO, memberEntity);
        } else {
            return new ResultBody<>();
        }


    }

    /**
     * 微博登录
     *
     * @param auth2SocialVO
     * @param memberEntity
     * @return
     */
    private ResultBody<MemberVO> getWeiboMemberVOResultBody(Auth2SocialVO auth2SocialVO, MemberEntity memberEntity) {
        if (memberEntity == null) {
            //如果是第一次登陆,拿到用户信息注册,
            String resultStr = HttpUtils.sendGet("https://api.weibo.com/2/users/show.json?" +
                    "access_token=" + auth2SocialVO.getAccess_token() + "&uid=" + auth2SocialVO.getUid());
            JSONObject jsonObject = JSON.parseObject(resultStr, JSONObject.class);
            if (jsonObject.get("error_code") == null) {
                //成功访问
                memberEntity = new MemberEntity();
                memberEntity.setNickname(jsonObject.getString("name"));//昵称
                memberEntity.setHeader(jsonObject.getString("profile_image_url"));//头像
                memberEntity.setGender("m".equals(jsonObject.getString("gender")) ? 1 : 2);//性别
                Long levelId = memberLevelDao.selectDefalutLevelId();
                memberEntity.setLevelId(levelId);//等级
                memberEntity.setAccessToken(auth2SocialVO.getAccess_token());
                memberEntity.setExpiresIn(auth2SocialVO.getExpires_in());
                memberEntity.setSourceType(1);//来源
                memberEntity.setSocialUid(auth2SocialVO.getUid());
                memberEntity.setIntegration(0);
                baseMapper.insert(memberEntity);
                MemberVO memberVO = new MemberVO();
                memberVO.setNickname(jsonObject.getString("name"));
                memberVO.setUserId(memberEntity.getId());
                memberVO.setIntegration(memberEntity.getIntegration());
                return new ResultBody<MemberVO>(memberVO);
            } else {
                return new ResultBody<>(GulimallExceptinCodeEnum.AUTH2_ERROR);
            }
        } else {
            memberEntity.setAccessToken(auth2SocialVO.getAccess_token());
            memberEntity.setExpiresIn(auth2SocialVO.getExpires_in());
            memberEntity.setSocialUid(auth2SocialVO.getUid());
            baseMapper.updateById(memberEntity);
            MemberVO memberVO = new MemberVO();
            memberVO.setUserId(memberEntity.getId());
            memberVO.setNickname(memberEntity.getNickname());
            memberVO.setIntegration(memberEntity.getIntegration());
            return new ResultBody<>(memberVO);
        }
    }


    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println("加密后:" + bCryptPasswordEncoder.encode("123456"));
        //$2a$10$Vt4.etgGcoO2hfxHJoxW8ezLML3qjG/W6P.SvbETZp/Y/904iJ1dG
        boolean matches = bCryptPasswordEncoder.matches("123456", "$2a$10$Vt4.etgGcoO2hfxHJoxW8ezLML3qjG/W6P.SvbETZp/Y/904iJ1dG");
        System.out.println("是否和密码123456匹配:" + matches);
        String nickName = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(nickName);
    }


}