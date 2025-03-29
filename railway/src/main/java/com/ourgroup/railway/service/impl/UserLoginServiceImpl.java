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

    // 登录功能
    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        // 查询数据库中的用户信息
        List<UserDO> users = userMapper.findByUsername(requestParam.getUsername());
    
        if (users == null || users.isEmpty()) {
            throw new RuntimeException("账号或密码错误");
        }
        
        UserDO userDO = users.get(0);
    
        // 验证密码
        if (!BCrypt.checkpw(requestParam.getPassword(), userDO.getPassword())) {
            throw new RuntimeException("账号或密码错误");
        }
            
        String accessToken = JWTUtil.generateAccessToken(userDO.getUsername(), userDO.getRealName());

        // 返回响应结果
        return new UserLoginRespDTO(userDO.getUsername(), userDO.getRealName(), accessToken);
    }

    // 注册功能
    @Override
    public UserRegisterRespDTO register(UserRegisterReqDTO requestParam) {
        // 检查用户名是否已存在
        List<UserDO> existingUsers = userMapper.findByUsername(requestParam.getUsername());
        
        if (existingUsers != null && !existingUsers.isEmpty()) {
            throw new RuntimeException("用户名已存在");
        }

        String hashedPassword = BCrypt.hashpw(requestParam.getPassword(), BCrypt.gensalt(12));

        // 创建新用户并插入到数据库
        UserDO newUser = new UserDO();
        newUser.setUsername(requestParam.getUsername());
        newUser.setPassword(hashedPassword);
        newUser.setRealName(requestParam.getRealName());

        try {
            userMapper.insert(newUser);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("注册失败，数据库错误");
        }

        // 返回注册响应
        return new UserRegisterRespDTO(newUser.getUsername(), newUser.getRealName());
    }
}
