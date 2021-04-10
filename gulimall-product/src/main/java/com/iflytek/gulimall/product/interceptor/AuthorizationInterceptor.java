package com.iflytek.gulimall.product.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Slf4j(topic = "AuthorizationInterceptor")
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
}
