package com.example.demo.config;

import com.p6spy.engine.spy.P6DataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(DataSourceProperties props) {
        // 1) build the real pool
        HikariDataSource hikari = props
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();

        // 2) wrap in P6Spy to log parameters
        DataSource p6spyDs = new P6DataSource(hikari);
        return p6spyDs;

        // 3) wrap in OpenTelemetryDataSource for spans
//        return new OpenTelemetryDataSource(p6spyDs);
    }
}