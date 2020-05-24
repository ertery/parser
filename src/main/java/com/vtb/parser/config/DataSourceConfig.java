package com.vtb.parser.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = {"com.vtb.parser.entity"})
@EnableJpaRepositories(basePackages = {"com.vtb.parser.repository"})
public class DataSourceConfig {

    @Value("${parser.postgresql.username}")
    private String username;
    @Value("${parser.postgresql.password}")
    private String password;
    @Value("${parser.postgresql.url}")
    private String postgresUrl;

    @Bean
    @Primary
    public DataSource dataSource() {
    /*    return DataSourceBuilder
                .create()
                .username(username)
                .password(password)
                .url(postgresUrl)
                .build();*/

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(postgresUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
