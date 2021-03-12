package com.iflytek.gulimall.seckill.interceptor;


import com.iflytek.gulimall.common.feign.vo.MemberVO;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

import static com.iflytek.gulimall.common.constant.AutherServerConstant.LOGIN_USER;
import static com.iflytek.gulimall.common.constant.AutherServerConstant.URL_LOGIN;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    public static ThreadLocal<MemberVO> toThreadLocal = new ThreadLocal<>();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        if (antPathMatcher.match("/seckill/getSecSessionSkuVOBySkuId/**", requestURI) ||
                antPathMatcher.match("/seckill/getCurrentSecSessionSkuVOS", requestURI) ) {
            return true;
        }
        Object attribute = request.getSession().getAttribute(LOGIN_USER);
        if (attribute == null) {
            String host = request.getHeader("X-Forwarded-Host");
            String returnUrl = "http://" + host + requestURI;
            String queryString = request.getQueryString();
            //判断url带不带参数,如果带拼接参数,编码到登录页
            if (!StringUtils.isEmpty(queryString)) {
                returnUrl = returnUrl + "?" + queryString;
            }
            String encodeReturnUrl = URLEncoder.encode(returnUrl, "utf-8");
            response.sendRedirect(URL_LOGIN + "?returnUrl=" + encodeReturnUrl);
            return false;
        } else {
            MemberVO memberVO = (MemberVO) attribute;
            toThreadLocal.set(memberVO);
            return true;

        }
    }
}
