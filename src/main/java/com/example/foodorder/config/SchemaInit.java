package com.example.foodorder.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SchemaInit implements CommandLineRunner {
    private final JdbcTemplate jdbc;
    public SchemaInit(JdbcTemplate jdbc){ this.jdbc = jdbc; }

    @Override public void run(String... args){
        jdbc.execute("CREATE TABLE IF NOT EXISTS tx_audit(" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "event_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "level VARCHAR(10)," +
                "category VARCHAR(64)," +
                "message VARCHAR(2000)," +
                "details VARCHAR(2000))");
    }
}
