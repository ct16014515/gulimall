package com.iflytek.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;


import com.iflytek.common.model.vo.memeber.MemberRegisterVO;
import com.iflytek.common.model.vo.memeber.MemberVO;
import com.iflytek.common.model.vo.memeber.UserLoginVO;
import com.iflytek.common.utils.ResultBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.iflytek.gulimall.member.entity.MemberEntity;
import com.iflytek.gulimall.member.service.MemberService;
import com.iflytek.common.utils.PageUtils;
import com.iflytek.common.utils.R;


/**
 * 会员
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 11:08:00
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id) {
        MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member) {
        memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member) {
        memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids) {
        memberService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 用户注册
     * @param memberRegisterVO
     * @return
     */
    @PostMapping("/register")
    public ResultBody register(@RequestBody MemberRegisterVO memberRegisterVO) {
        ResultBody resultBody= memberService.register(memberRegisterVO);
        return resultBody;
    }


    /**
     * 用户登录
     * @param userLoginVO
     * @return
     */
    @PostMapping("/register")
    public ResultBody<MemberVO> login(@RequestBody UserLoginVO userLoginVO) {
        ResultBody<MemberVO> resultBody= memberService.login(userLoginVO);
        return resultBody;
    }





}
