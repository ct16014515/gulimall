package com.iflytek.gulimall.member.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.iflytek.gulimall.common.feign.vo.*;
import com.iflytek.gulimall.common.utils.ResultBody;

import com.iflytek.gulimall.common.feign.MemberServiceAPI;
import com.iflytek.gulimall.member.entity.MemberReceiveAddressEntity;
import com.iflytek.gulimall.member.service.MemberReceiveAddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.iflytek.gulimall.member.entity.MemberEntity;
import com.iflytek.gulimall.member.service.MemberService;
import com.iflytek.gulimall.common.utils.PageUtils;
import com.iflytek.gulimall.common.utils.R;


/**
 * 会员
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 11:08:00
 */
@RestController
@RequestMapping("member/member")
public  class MemberController implements MemberServiceAPI {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberReceiveAddressService memberReceiveAddressService;


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
     *
     * @param
     * @return
     */
    @PostMapping("/register")
    public ResultBody memberRegister(@RequestBody RegisterVO registerVO) {
        ResultBody resultBody = memberService.register(registerVO);
        return resultBody;
    }


    /**
     * 用户登录
     *
     * @param userLoginVO
     * @return
     */
    @PostMapping("/login")
    public ResultBody<MemberVO> memberLogin(@RequestBody UserLoginVO userLoginVO) {
        ResultBody<MemberVO> resultBody = memberService.login(userLoginVO);
        return resultBody;
    }

    /**
     * @param auth2SocialVO
     * @return
     */
    @PostMapping("auth2login")
    public ResultBody<MemberVO> auth2Login(@RequestBody Auth2SocialVO auth2SocialVO) {
        ResultBody<MemberVO> resultBody = memberService.auth2Login(auth2SocialVO);
        return resultBody;
    }

    @GetMapping("/getMemberReceiveAddressByUid/{uid}")
    public ResultBody<List<MemberReceiveAddressVO>> getMemberReceiveAddressByUid(@PathVariable("uid") Long uid) {
        List<MemberReceiveAddressVO> list = memberReceiveAddressService.getMemberReceiveAddressByUid(uid);
        return new ResultBody<>(list);
    }

    /**
     * 信息
     */
    @RequestMapping("/getMemberreceiveaddressById/{id}")
    // @RequiresPermissions("member:memberreceiveaddress:info")
    public ResultBody<MemberReceiveAddressVO> getMemberreceiveaddressById(@PathVariable("id") Long id) {
        MemberReceiveAddressEntity memberReceiveAddress = memberReceiveAddressService.getById(id);
        MemberReceiveAddressVO memberReceiveAddressVO = new MemberReceiveAddressVO();
        BeanUtils.copyProperties(memberReceiveAddress,memberReceiveAddressVO);
        return new ResultBody<>(memberReceiveAddressVO);
    }

}
