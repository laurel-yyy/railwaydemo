package com.ourgroup.railway.model.dao;

import lombok.Data; 

@Data
public class UserDO {
    
    private Long id;       

    private String username;  

    private String password; 

    private String realName; 
}