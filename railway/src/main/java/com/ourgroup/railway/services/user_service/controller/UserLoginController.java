package com.ourgroup.railway.services.user_service.controller;

import com.ourgroup.railway.services.user_service.dto.req.UserLoginReqDTO;
import com.ourgroup.railway.services.user_service.dto.req.UserRegisterReqDTO;
import com.ourgroup.railway.services.user_service.dto.resp.UserLoginRespDTO;
import com.ourgroup.railway.services.user_service.dto.resp.UserRegisterRespDTO;
import com.ourgroup.railway.services.user_service.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserLoginController {

    @Autowired
    private UserLoginService userLoginService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public UserLoginRespDTO login(@RequestBody UserLoginReqDTO requestParam) {
        return userLoginService.login(requestParam);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public UserRegisterRespDTO register(@RequestBody UserRegisterReqDTO requestParam) {
        return userLoginService.register(requestParam);
    }
}
