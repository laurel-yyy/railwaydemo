package com.ourgroup.railway.model.dto.resp;

public class UserRegisterRespDTO {

    private String username;
    private String realName;

    // Constructor, getters and setters
    public UserRegisterRespDTO(String username, String realName) {
        this.username = username;
        this.realName = realName;
    }
}
