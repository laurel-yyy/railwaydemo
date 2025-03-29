package com.ourgroup.railway.model.dao;

import lombok.Data; // Lombok annotation to generate getters, setters, and other methods

@Data
public class UserDO {
    
    private Long id;          // Auto-increment ID

    private String username;  // Username

    private String password;  // Password

    private String realName;  // Real name
}