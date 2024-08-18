package com.example.demo.config;

public class AppConfig {

    @Bean
    public Jdbi jdbc(Datasource ds) {
        return Jdbc.create(ds);
    }
}
