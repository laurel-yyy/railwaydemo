package com.ourgroup.railway.service;

import com.ourgroup.railway.model.dto.req.UserLoginReqDTO;
import com.ourgroup.railway.model.dto.req.UserRegisterReqDTO;
import com.ourgroup.railway.model.dto.resp.UserLoginRespDTO;
import com.ourgroup.railway.model.dto.resp.UserRegisterRespDTO;

/**
 * 用户登录和注册服务接口
 */
public interface UserLoginService {

    /**
     * 登录功能
     * 
     * @param requestParam 用户登录请求参数
     * @return 登录响应结果
     */
    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    /**
     * 注册功能
     * 
     * @param requestParam 用户注册请求参数
     * @return 注册响应结果
     */
    UserRegisterRespDTO register(UserRegisterReqDTO requestParam);
}
