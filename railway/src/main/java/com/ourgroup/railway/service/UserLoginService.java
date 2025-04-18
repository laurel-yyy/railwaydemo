package com.ourgroup.railway.service;

import com.ourgroup.railway.model.dto.req.UserLoginReqDTO;
import com.ourgroup.railway.model.dto.req.UserRegisterReqDTO;
import com.ourgroup.railway.model.dto.resp.UserLoginRespDTO;
import com.ourgroup.railway.model.dto.resp.UserRegisterRespDTO;

/**
 * User Login and Registration Service Interface
 */
public interface UserLoginService {
    /**
     * Login functionality
     * 
     * @param requestParam User login request parameters
     * @return Login response result
     */
    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    /**
     * Registration functionality
     * 
     * @param requestParam User registration request parameters
     * @return Registration response result
     */
    UserRegisterRespDTO register(UserRegisterReqDTO requestParam);
}