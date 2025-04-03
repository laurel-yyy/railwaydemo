package com.ourgroup.railway.model.dto.resp;
import lombok.Data; // Lombok annotation to generate getters, setters, and other methods

@Data
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
