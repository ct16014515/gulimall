package com.iflytek.gulimall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GulimallCorsConfiguration {
    /**
     * cors跨域满足携带cookie的生效条件
     * 1、 configuration.setAllowCredentials(true);
     * 2、 configuration.addAllowedOrigin("http://localhost:1000"); 允许的域,不能写*,否则cookie失效
     * @return
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        //允许的头信息
        configuration.addAllowedHeader("*");
        //允许请求方式
        configuration.addAllowedMethod("*");
        //允许的域,不能写*,否则cookie失效
        configuration.addAllowedOrigin("*");
        //是否允许携带cookie信息
        configuration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", configuration);
        CorsWebFilter corsWebFilter = new CorsWebFilter(source);
        return corsWebFilter;
    }


}
