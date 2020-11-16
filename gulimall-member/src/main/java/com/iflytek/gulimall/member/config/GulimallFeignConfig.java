package com.iflytek.gulimall.member.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 1、feign调用会丢失请求头,所以在线程开启子线程RequestContextHolder上下文加入setRequestAttributes
 * 2、如果开启熔断器 feign.hystrix.enabled: true 默认的隔离策略是线程隔离 导致ThreadLocal不会传播,即在执行apply时,从上下文拿不到RequestAttributes
 * 3、编写自定义并发策略RequestAttributeHystrixConcurrencyStrategy，在执行方法上加入getRequestAttributes
 */
@Configuration
@Slf4j
public class GulimallFeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (requestAttributes != null) {
                    HttpServletRequest request = requestAttributes.getRequest();
                    String cookie = request.getHeader("Cookie");
                    template.header("Cookie", cookie);
                }

            }
        };
    }

}
