package com.iflytek.gulimall.order.interceptor;


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
    public static ThreadLocal<MemberVO> THREAD_LOCAL_MEMBER = new ThreadLocal<>();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        if (antPathMatcher.match("/order/**", requestURI) ||
                antPathMatcher.match("/order/aliPayNotifyUrl/**", requestURI) ||
                antPathMatcher.match("/order/wxPayNotifyUrl/**", requestURI)) {
            return true;
        }
        Object attribute = request.getSession().getAttribute(LOGIN_USER);
        if (attribute == null) {
            String host = request.getHeader("X-Forwarded-Host");
            String returnUrl = "http://" + host + requestURI;
            String queryString = request.getQueryString();
            if (!StringUtils.isEmpty(queryString)) {
                returnUrl = returnUrl + "?" + queryString;
            }
            String encodeReturnUrl = URLEncoder.encode(returnUrl, "utf-8");
            response.sendRedirect(URL_LOGIN + "?returnUrl=" + encodeReturnUrl);
            return false;
        } else {
            MemberVO memberVO = (MemberVO) attribute;
            THREAD_LOCAL_MEMBER.set(memberVO);
            return true;
        }
    }

    public static MemberVO getMemberVO() {
        return THREAD_LOCAL_MEMBER.get();
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 必须手动清除ThreadLocal中线程变量,因为使用的是tomcat的线程池
        THREAD_LOCAL_MEMBER.remove();
    }


}
