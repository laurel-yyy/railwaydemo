package com.ourgroup.railway.service.impl;


import com.ourgroup.railway.model.dao.UserDO;
import com.ourgroup.railway.mapper.*;
import com.ourgroup.railway.model.dto.req.UserLoginReqDTO;
import com.ourgroup.railway.model.dto.req.UserRegisterReqDTO;
import com.ourgroup.railway.model.dto.resp.UserLoginRespDTO;
import com.ourgroup.railway.model.dto.resp.UserRegisterRespDTO;
import com.ourgroup.railway.service.UserLoginService;
import com.ourgroup.railway.framework.toolkit.JWTUtil;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private UserMapper userMapper;

    // Login
    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        // search user
        List<UserDO> users = userMapper.findByUsername(requestParam.getUsername());
    
        if (users == null || users.isEmpty()) {
            throw new RuntimeException("账号或密码错误");
        }
        
        UserDO userDO = users.get(0);
    
        // password
        if (!BCrypt.checkpw(requestParam.getPassword(), userDO.getPassword())) {
            throw new RuntimeException("账号或密码错误");
        }
            
        String accessToken = JWTUtil.generateAccessToken(String.valueOf(userDO.getId()));

        // 返回响应结果
        return new UserLoginRespDTO(userDO.getUsername(), userDO.getRealName(), accessToken);
    }

    // Login Feature
    @Override
    public UserRegisterRespDTO register(UserRegisterReqDTO requestParam) {
        // check if the name already exists
        List<UserDO> existingUsers = userMapper.findByUsername(requestParam.getUsername());
        
        if (existingUsers != null && !existingUsers.isEmpty()) {
            throw new RuntimeException("User already exists");
        }

        String hashedPassword = BCrypt.hashpw(requestParam.getPassword(), BCrypt.gensalt(12));

        // create new user
        UserDO newUser = new UserDO();
        newUser.setId(System.currentTimeMillis());
        newUser.setUsername(requestParam.getUsername());
        newUser.setPassword(hashedPassword);
        newUser.setRealName(requestParam.getRealName());

        try {
            userMapper.insert(newUser);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Register failed");
        }

        // return response
        return new UserRegisterRespDTO(newUser.getUsername(), newUser.getRealName());
    }
}
