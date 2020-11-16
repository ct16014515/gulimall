package com.iflytek.gulimall.common.feign;



import com.iflytek.gulimall.common.feign.vo.*;
import com.iflytek.gulimall.common.utils.ResultBody;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "gulimall-member")
public interface MemberServiceAPI {
    @PostMapping("member/member/register")
    public ResultBody memberRegister(@RequestBody RegisterVO registerVO);
    @PostMapping("member/member/login")
    public ResultBody<MemberVO>memberLogin(@RequestBody UserLoginVO userLoginVO);

    @PostMapping("member/member/auth2login")
    public ResultBody<MemberVO>auth2Login(@RequestBody Auth2SocialVO auth2SocialVO);
    @GetMapping("member/member/getMemberReceiveAddressByUid/{uid}")
    public ResultBody<List<MemberReceiveAddressVO>> getMemberReceiveAddressByUid(@PathVariable("uid") Long uid);
    @GetMapping("member/member/getMemberreceiveaddressById/{id}")
    public ResultBody<MemberReceiveAddressVO> getMemberreceiveaddressById(@PathVariable("id") Long id);
}
