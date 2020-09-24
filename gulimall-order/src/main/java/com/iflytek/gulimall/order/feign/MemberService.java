package com.iflytek.gulimall.order.feign;

import com.iflytek.common.utils.R;
import com.iflytek.common.utils.ResultBody;
import com.iflytek.gulimall.order.vo.MemberReceiveAddressVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("gulimall-member")
public interface MemberService {

    @GetMapping("member/memberreceiveaddress/getMemberReceiveAddressByUid/{uid}")
    public ResultBody<List<MemberReceiveAddressVO>> getMemberReceiveAddressByUid(@PathVariable("uid") Long uid);
    @GetMapping("member/memberreceiveaddress/info/{id}")
    public ResultBody<MemberReceiveAddressVO> info(@PathVariable("id") Long id);
}
