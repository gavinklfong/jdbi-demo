package com.example.demo.config;

import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

public class AppConfig {

    @Bean
    public Jdbi jdbi(DataSource ds) {
        return Jdbi.create(ds);
    }
}
