package com.iflytek.gulimall.product.config;

import com.iflytek.gulimall.product.interceptor.AuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/dict/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/permission/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/role/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/platform_user/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/activity/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/after/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/buyer/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/classify/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/comment/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/express/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/finance/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/label/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/sensitive/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/check/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/shop/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/withdrawal/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/message/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/word/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/canvas/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/buyer_withdrawal/**");
    }
}
