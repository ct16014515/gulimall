package com.iflytek.gulimall.member.interceptor;



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
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String requestURI1 = request.getRequestURI();
        boolean match = antPathMatcher.match("/member/member/**", requestURI1);
        if (match){
            return true;
        }
        Object attribute = request.getSession().getAttribute(LOGIN_USER);


        if (attribute == null) {
            String host = request.getHeader("X-Forwarded-Host");
            String requestURI = request.getRequestURI();
            String returnUrl = "http://" + host + requestURI;
            String queryString = request.getQueryString();
            //判断url带不带参数
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
