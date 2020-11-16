package com.iflytek.gulimall.product;

import com.iflytek.gulimall.product.config.DataSourceProxyAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;


@MapperScan("com.iflytek.gulimall.product.dao")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.iflytek.gulimall.common.feign"})
@EnableCaching
@EnableRedisHttpSession
@Import({DataSourceProxyAutoConfiguration.class})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},
        scanBasePackages = {"com.iflytek.gulimall.common.feign","com.iflytek.gulimall.product"})
@Slf4j
public class GulimallProductApplication {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(GulimallProductApplication.class, args);
        Environment env = application.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "应用 '{}' 运行成功! 访问连接:\n\t" +
                        "数据库监控: \t\thttp://{}:{}{}/druid\n" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                StringUtils.isEmpty(env.getProperty("server.servlet.context-path"))?"":env.getProperty("server.servlet.context-path"));
    }


}
