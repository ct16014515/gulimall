package com.iflytek.gulimall.order.interceptor;

import com.iflytek.common.model.vo.memeber.MemberVO;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import static com.iflytek.common.constant.AutherServerConstant.*;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    public static ThreadLocal<MemberVO> toThreadLocal = new ThreadLocal<>();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI1 = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean match = antPathMatcher.match("/order/order/getOrderEntityByOrderSn/**", requestURI1);
        if (match){
            return true;
        }

        Object attribute = request.getSession().getAttribute(LOGIN_USER);
        if (attribute == null) {
            String host = request.getHeader("X-Forwarded-Host");
            String requestURI = requestURI1;
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
