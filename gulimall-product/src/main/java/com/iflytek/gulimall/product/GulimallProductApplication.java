package com.iflytek.gulimall.product;

import com.iflytek.gulimall.product.config.SpringContextHolder;
import com.iflytek.gulimall.product.config.DataSourceProxyAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.net.InetAddress;
import java.net.UnknownHostException;


@MapperScan("com.iflytek.gulimall.product.dao")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.iflytek.gulimall.common.feign"})
@EnableRedisHttpSession
@Import({DataSourceProxyAutoConfiguration.class})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},
        scanBasePackages = {"com.iflytek.gulimall.common.feign", "com.iflytek.gulimall.product"})
@Slf4j
public class GulimallProductApplication {
    public static void main(String[] args) throws UnknownHostException {
        ApplicationContext applicationContext = SpringApplication.run(GulimallProductApplication.class, args);
        SpringContextHolder springContextHolder = new SpringContextHolder();
        springContextHolder.setApplicationContext(applicationContext);
        Environment env = applicationContext.getEnvironment();
        log.info("\n-----------------------------------\n\t" +
                        "应用 '{}'运行成功,\n\t" +
                        "Swagger文档: \t\thttp://{}:{}/doc.html\n\t" +
                        "-----------------------------------------------",
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port")
        );
    }


}
