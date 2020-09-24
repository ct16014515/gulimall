package com.iflytek.gulimall.autherserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients//feign的客户端,远程调用使用
@EnableRedisHttpSession//整合redis作为session存储
public class GulimallAutherServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallAutherServerApplication.class, args);
    }

}
