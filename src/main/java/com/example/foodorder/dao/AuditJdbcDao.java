package com.example.foodorder.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuditJdbcDao {
    private final JdbcTemplate jdbcTemplate;
    public AuditJdbcDao(JdbcTemplate jdbcTemplate){ this.jdbcTemplate = jdbcTemplate; }

    public void log(String level, String category, String message, String details){
        jdbcTemplate.update(
            "INSERT INTO tx_audit(level, category, message, details) VALUES (?,?,?,?)",
            level, category, message, details
        );
    }
}
