package com.ifast.ipaymy.backend.util.observability.config;

import com.ifast.ipaymy.backend.util.constant.SharedConstant;
import com.p6spy.engine.spy.P6DataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @Profile(SharedConstant.LOCAL)
    public DataSource dataSource(DataSourceProperties props) {
        // 1) build the real pool
        HikariDataSource hikari = props
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();

        // 2) wrap in P6Spy to log parameters
        return new P6DataSource(hikari);
    }
}
