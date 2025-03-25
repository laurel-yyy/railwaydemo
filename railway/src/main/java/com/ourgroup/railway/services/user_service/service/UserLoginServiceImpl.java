package com.ourgroup.railway.services.user_service.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ourgroup.railway.services.user_service.dao.UserDO;
import com.ourgroup.railway.services.user_service.dao.UserMapper;
import com.ourgroup.railway.services.user_service.dto.req.UserLoginReqDTO;
import com.ourgroup.railway.services.user_service.dto.req.UserRegisterReqDTO;
import com.ourgroup.railway.services.user_service.dto.resp.UserLoginRespDTO;
import com.ourgroup.railway.services.user_service.dto.resp.UserRegisterRespDTO;
import com.ourgroup.railway.services.user_service.service.UserLoginService;
import com.ourgroup.railway.framework.toolkit.JWTUtil;
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
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .eq(UserDO::getPassword, requestParam.getPassword());

        UserDO userDO = userMapper.selectOne(queryWrapper);

        if (userDO != null) {
            // 生成JWT令牌
            String accessToken = JWTUtil.generateAccessToken(userDO.getUsername(), userDO.getRealName());

            // 返回响应结果
            return new UserLoginRespDTO(userDO.getUsername(), userDO.getRealName(), accessToken);
        } else {
            throw new RuntimeException("账号或密码错误");
        }
    }

    // 注册功能
    @Override
    public UserRegisterRespDTO register(UserRegisterReqDTO requestParam) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername());
        UserDO existingUser = userMapper.selectOne(queryWrapper);

        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建新用户并插入到数据库
        UserDO newUser = new UserDO();
        newUser.setUsername(requestParam.getUsername());
        newUser.setPassword(requestParam.getPassword());
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
