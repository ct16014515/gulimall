package com.iflytek.gulimall.autherserver.feign.factory;

import com.iflytek.common.exception.GulimallExceptinCodeEnum;
import com.iflytek.common.model.vo.memeber.MemberVO;
import com.iflytek.common.model.vo.memeber.UserLoginVO;
import com.iflytek.common.utils.ResultBody;
import com.iflytek.gulimall.autherserver.vo.RegisterVO;
import com.iflytek.gulimall.autherserver.feign.MemberService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MemberServiceFallbackFactory implements FallbackFactory<MemberService> {
    @Override
    public MemberService create(Throwable cause) {
        return new MemberService() {
            @Override
            public ResultBody memberRegister(RegisterVO registerVO) {
                log.info("触发熔断,原因:{}", cause);
                return new ResultBody(GulimallExceptinCodeEnum.FEIGN_ERROR, null);
            }

            @Override
            public ResultBody<MemberVO> memberLogin(UserLoginVO userLoginVO) {
                return null;
            }
        };
    }
}
