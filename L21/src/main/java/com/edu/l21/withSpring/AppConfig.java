package com.edu.l21.withSpring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        DataSource ds = new DataSource();
        ds.setUrl("jdbc:postgresql://localhost:5432/mydb");
        ds.setUsername("user");
        ds.setPassword("pass");
        return ds;
    }

    @Bean
    public ConnectionPool connectionPool(DataSource dataSource) {
        return new ConnectionPool(dataSource);
    }
}
