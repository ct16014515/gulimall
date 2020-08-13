package com.iflytek.gulimall.member.service.impl;

import com.iflytek.common.exception.GulimallExceptinCodeEnum;
import com.iflytek.common.model.vo.memeber.MemberRegisterVO;
import com.iflytek.common.model.vo.memeber.MemberVO;
import com.iflytek.common.model.vo.memeber.UserLoginVO;
import com.iflytek.common.utils.ResultBody;
import com.iflytek.gulimall.member.dao.MemberLevelDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.common.utils.PageUtils;
import com.iflytek.common.utils.Query;

import com.iflytek.gulimall.member.dao.MemberDao;
import com.iflytek.gulimall.member.entity.MemberEntity;
import com.iflytek.gulimall.member.service.MemberService;

import static com.iflytek.common.exception.GulimallExceptinCodeEnum.MEMBER_USERNAME_NOTEXIT_ERROR;


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
     * @param memberRegisterVO
     * @return
     */
    @Override
    public ResultBody register(MemberRegisterVO memberRegisterVO) {
        //查询此手机号和姓名是否已经存在
        Integer phoneCount = memberDao.selectCountByPhone(memberRegisterVO.getPhone());
        if (phoneCount > 0) {
            return new ResultBody(GulimallExceptinCodeEnum.MEMBER_PHONE_EXIT_ERROR, null);
        }
        //查询此手机号和姓名是否已经存在
        Integer usernameCount = memberDao.selectCountByUsername(memberRegisterVO.getUsername());
        if (usernameCount > 0) {
            return new ResultBody(GulimallExceptinCodeEnum.MEMBER_USERNAME_EXIT_ERROR, null);
        }
        //查询用户等级
        Long levelId = memberLevelDao.selectDefalutLevelId();
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setLevelId(levelId);
        String password = memberRegisterVO.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePassword = bCryptPasswordEncoder.encode(password);
        memberEntity.setPassword(encodePassword);
        BeanUtils.copyProperties(memberRegisterVO, memberEntity);
        this.save(memberEntity);
        return new ResultBody(0, "success", null);
    }

    /**
     * 用户登录
     *
     * @param userLoginVO
     * @return
     */
    @Override
    public ResultBody<MemberVO> login(UserLoginVO userLoginVO) {
        MemberEntity memberEntity = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("username", userLoginVO.getUsername()));
        if (memberEntity == null) {
            return new ResultBody(GulimallExceptinCodeEnum.MEMBER_USERNAME_NOTEXIT_ERROR, null);
        }
        String password = memberEntity.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches(userLoginVO.getPassword(), password);
        if (!matches) {
            return new ResultBody(GulimallExceptinCodeEnum.MEMBER_PASSWORD_ERROR, null);
        }
        MemberVO memberVO = new MemberVO();
        BeanUtils.copyProperties(memberEntity, memberVO);
        return new ResultBody(0, "success", memberVO);
    }


    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println("加密后:" + bCryptPasswordEncoder.encode("123456"));
        //$2a$10$Vt4.etgGcoO2hfxHJoxW8ezLML3qjG/W6P.SvbETZp/Y/904iJ1dG
        boolean matches = bCryptPasswordEncoder.matches("123456", "$2a$10$Vt4.etgGcoO2hfxHJoxW8ezLML3qjG/W6P.SvbETZp/Y/904iJ1dG");
        System.out.println("是否和密码123456匹配:" + matches);
    }


}