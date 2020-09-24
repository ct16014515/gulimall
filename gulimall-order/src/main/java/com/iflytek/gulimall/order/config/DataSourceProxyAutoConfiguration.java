package com.iflytek.gulimall.order.config;

        import com.alibaba.druid.pool.DruidDataSource;
        import com.zaxxer.hikari.HikariDataSource;
        import io.seata.rm.datasource.DataSourceProxy;
        import org.apache.ibatis.session.SqlSessionFactory;
        import org.mybatis.spring.SqlSessionFactoryBean;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
        import org.springframework.boot.context.properties.ConfigurationProperties;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.context.annotation.Primary;
        import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
        import org.springframework.util.StringUtils;

        import javax.sql.DataSource;

@Configuration
public class DataSourceProxyAutoConfiguration {


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }

    @Primary
    @Bean
    public DataSourceProxy dataSourceProxy(DruidDataSource druidDataSource) {
        return new DataSourceProxy(druidDataSource);

    }

}
