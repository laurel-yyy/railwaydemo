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

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        List<UserDO> users = userMapper.findByUsername(requestParam.getUsername());
    
        if (users == null || users.isEmpty()) {
            throw new RuntimeException("password or username error");
        }
        
        UserDO userDO = users.get(0);
    
        if (!BCrypt.checkpw(requestParam.getPassword(), userDO.getPassword())) {
            throw new RuntimeException("password or username error");
        }
            
        String accessToken = JWTUtil.generateAccessToken(String.valueOf(userDO.getId()));

        return new UserLoginRespDTO(userDO.getUsername(), userDO.getRealName(), accessToken);
    }

    @Override
    public UserRegisterRespDTO register(UserRegisterReqDTO requestParam) {
        // check if the name already exists
        List<UserDO> existingUsers = userMapper.findByUsername(requestParam.getUsername());
        
        if (existingUsers != null && !existingUsers.isEmpty()) {
            throw new RuntimeException("User already exists");
        }

        String hashedPassword = BCrypt.hashpw(requestParam.getPassword(), BCrypt.gensalt(12));

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

        return new UserRegisterRespDTO(newUser.getUsername(), newUser.getRealName());
    }
}
