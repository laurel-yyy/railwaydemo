package com.ourgroup.railway.services.user_service;
import org.mybatis.spring.annotation.MapperScan; // Importing MapperScan for MyBatis mapper scanning

@MapperScan("com.ourgroup.railway.services.user_service.dao") // Scans the specified package for MyBatis mapper interfaces
public class UserServiceApplication {
    
}
