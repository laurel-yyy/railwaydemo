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

/**
 * JWT认证切面 - 用于拦截需要认证的API请求，验证JWT令牌
 */
@Aspect         // 声明这是一个切面类
@Component      // 将切面类注册为Spring组件
public class JwtAuthenticationAspect {

    // 定义一个切点，匹配控制器包下的所有方法，但排除登录和注册方法
    @Pointcut("execution(* com.ourgroup.railway.controller.*.*(..)) && " +
              "!execution(* com.ourgroup.railway.controller.UserLoginController.login(..)) && " +
              "!execution(* com.ourgroup.railway.controller.UserLoginController.register(..))" )
    public void authenticatedOperations() {}

    /**
     * 环绕通知 - 在目标方法执行前后都可以添加自定义行为
     * 
     * @param joinPoint 连接点，包含被拦截方法的信息
     * @return 原方法的返回值或错误响应
     */
    @Around("authenticatedOperations()")
    public Object authenticate(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取当前请求对象
        // return joinPoint.proceed();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        
        // 从请求头中获取Authorization字段的值
        String authHeader = request.getHeader("Authorization");
        
        // 检查是否有Authorization头，并且是否以Bearer开头
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error("缺少有效的认证令牌");
        }
        
        // 提取JWT令牌 (去掉"Bearer "前缀)
        String token = authHeader.substring(7);
        
        // 验证令牌
        if (!JWTUtil.validateToken(token)) {
            return Result.error("无效的认证令牌或令牌已过期");
        }
        
        try {
            // 从有效令牌中提取用户ID
            String userId = JWTUtil.getUserIdFromToken(token);
            
            // 将用户ID存储在请求属性中，供后续方法使用
            request.setAttribute("userId", userId);
            
            // 认证通过，继续执行原始方法
            return joinPoint.proceed();
            
        } catch (Exception e) {
            // 处理令牌解析过程中的任何异常
            return Result.error("令牌处理异常: " + e.getMessage());
        }
    }
    
    /**
     * 辅助方法 - 在控制器中获取当前用户ID
     */
    public static String getCurrentUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return (String) request.getAttribute("userId");
    }
}