package com.iflytek.gulimall.product.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class GulimallOrderDatasourceConfig {
    @Value("${gulimall.order.url:''}")
    private String url;
    @Value("${gulimall.order.username:''}")
    private String username;
    @Value("${gulimall.order.password:''}")
    private String password;
    @Value("${gulimall.order.driver-class-name:''}")
    private String driverClassName;
    //配置数据源
    @Bean(name = "orderDataSource")
    public DataSource orderDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxActive(256);
        dataSource.setInitialSize(16);
        dataSource.setMaxWait(60000);
        dataSource.setMinIdle(16);
        dataSource.setTimeBetweenEvictionRunsMillis(3000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        return dataSource;
    }
    @Bean(name = "orderDataSourceTransactionManager")
    public DataSourceTransactionManager orderDataSourceTransactionManager(@Qualifier("orderDataSource") DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }


}
