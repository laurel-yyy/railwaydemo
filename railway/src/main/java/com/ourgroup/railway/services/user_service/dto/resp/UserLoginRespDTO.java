package com.ourgroup.railway.services.user_service.dto.resp;

public class UserLoginRespDTO {

    private String username;
    private String realName;
    private String accessToken;

    // Constructor, getters and setters
    public UserLoginRespDTO(String username, String realName, String accessToken) {
        this.username = username;
        this.realName = realName;
        this.accessToken = accessToken;
    }
}
