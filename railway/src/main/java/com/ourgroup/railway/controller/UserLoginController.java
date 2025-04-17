package com.ourgroup.railway.controller;

import com.ourgroup.railway.framework.result.Result;
import com.ourgroup.railway.model.dto.req.UserLoginReqDTO;
import com.ourgroup.railway.model.dto.req.UserRegisterReqDTO;
import com.ourgroup.railway.model.dto.resp.UserLoginRespDTO;
import com.ourgroup.railway.model.dto.resp.UserRegisterRespDTO;
import com.ourgroup.railway.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserLoginController {

    @Autowired
    private UserLoginService userLoginService;

    /**
     * User login
     */
    @PostMapping("/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO requestParam) {
        UserLoginRespDTO userLogin = userLoginService.login(requestParam);
        return Result.success(userLogin);
    }

    /**
     * User registration
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody UserRegisterReqDTO requestParam) {
        userLoginService.register(requestParam);
        return Result.success();
    }
}
