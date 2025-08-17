package com.example.foodorder.config;

import com.example.foodorder.dao.AuditJdbcDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final AuditJdbcDao audit;
    public GlobalExceptionHandler(AuditJdbcDao audit){ this.audit=audit; }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String,Object>> badRequest(Exception e){
        log.warn("Bad request: {}", e.getMessage());
        audit.log("ERROR","API",e.getMessage(), null);
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> internal(Exception e){
        log.error("Unexpected error", e);
        audit.log("ERROR","API","Unexpected", e.toString());
        return ResponseEntity.status(500).body(Map.of("error","Internal server error"));
    }
}
