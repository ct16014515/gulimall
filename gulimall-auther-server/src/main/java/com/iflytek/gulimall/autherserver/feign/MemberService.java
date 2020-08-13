package com.iflytek.gulimall.autherserver.feign;

import com.iflytek.common.model.vo.memeber.MemberVO;
import com.iflytek.common.model.vo.memeber.UserLoginVO;
import com.iflytek.common.utils.ResultBody;
import com.iflytek.gulimall.autherserver.vo.RegisterVO;
import com.iflytek.gulimall.autherserver.feign.factory.MemberServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "gulimall-member", fallbackFactory = MemberServiceFallbackFactory.class)
public interface MemberService {
    @PostMapping("member/member/register")
    public ResultBody memberRegister(@RequestBody RegisterVO registerVO);
    @PostMapping("member/member/login")
    public ResultBody<MemberVO>memberLogin(@RequestBody UserLoginVO userLoginVO);

}
