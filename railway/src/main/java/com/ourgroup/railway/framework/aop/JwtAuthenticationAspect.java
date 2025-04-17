package com.ourgroup.railway.framework.aop;

import com.ourgroup.railway.framework.result.Result;
import com.ourgroup.railway.framework.toolkit.JWTUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;


@Aspect        
@Component     
public class JwtAuthenticationAspect {


    @Pointcut("execution(* com.ourgroup.railway.controller.*.*(..)) && " +
              "!execution(* com.ourgroup.railway.controller.UserLoginController.login(..)) && " +
              "!execution(* com.ourgroup.railway.controller.UserLoginController.register(..))" )
    public void authenticatedOperations() {}


    @Around("authenticatedOperations()")
    public Object authenticate(ProceedingJoinPoint joinPoint) throws Throwable {
  
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
 
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error("invalid authentication header");
        }

        String token = authHeader.substring(7);
 
        if (!JWTUtil.validateToken(token)) {
            return Result.error("invalid token");
        }
        
        try {
            String userId = JWTUtil.getUserIdFromToken(token);

            request.setAttribute("userId", userId);
            
            return joinPoint.proceed();
            
        } catch (Exception e) {
            return Result.error("invalid token: " + e.getMessage());
        }
    }
    
    public static String getCurrentUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return (String) request.getAttribute("userId");
    }
}