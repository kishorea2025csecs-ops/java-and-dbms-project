package com.parkease.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Intelligent DataSource Configurator:
 * Checks if MySQL 8 on localhost:3306 is reachable.
 * If MySQL is reachable, connects to MySQL 'parkease_db'.
 * If MySQL is offline, gracefully falls back to H2 in-memory MySQL-compatibility mode
 * so the application starts and serves localhost:8080 smoothly!
 */
@Configuration
public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/parkease_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true}")
    private String mysqlUrl;

    @Value("${spring.datasource.username:root}")
    private String mysqlUser;

    @Value("${spring.datasource.password:root}")
    private String mysqlPassword;

    @Value("${spring.datasource.driver-class-name:com.mysql.cj.jdbc.Driver}")
    private String mysqlDriver;

    @Bean
    @Primary
    public DataSource dataSource() {
        if (isMySqlAvailable()) {
            log.info("=========================================================");
            log.info("  CONNECTED TO LOCAL MYSQL 8 DATABASE (parkease_db)");
            log.info("=========================================================");
            return DataSourceBuilder.create()
                    .driverClassName(mysqlDriver)
                    .url(mysqlUrl)
                    .username(mysqlUser)
                    .password(mysqlPassword)
                    .build();
        } else {
            log.warn("=========================================================");
            log.warn("  MySQL on localhost:3306 is not currently reachable.");
            log.warn("  Enabling built-in in-memory fallback database.");
            log.warn("  All 20 slots, OOP logic & features are active!");
            log.warn("=========================================================");
            return DataSourceBuilder.create()
                    .driverClassName("org.h2.Driver")
                    .url("jdbc:h2:mem:parkease_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL")
                    .username("sa")
                    .password("")
                    .build();
        }
    }

    private boolean isMySqlAvailable() {
        try {
            Class.forName(mysqlDriver);
            DriverManager.setLoginTimeout(2);
            try (Connection conn = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword)) {
                return conn != null && !conn.isClosed();
            }
        } catch (Exception ex) {
            return false;
        }
    }
}

