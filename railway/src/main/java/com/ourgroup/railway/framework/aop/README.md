aop使用方法示例：
首先切面定义了切点 authenticatedOperations()，它会自动拦截所有控制器方法（除了登录和注册方法）。所以不需要在每个控制器方法上添加特殊的注解或标识。
其次，在控制其中的使用方法：
@RestController
@RequestMapping("/api/example")
public class ExampleController {

    @PostMapping("/action")
    public Result<String> doSomething(@RequestBody SomeDTO requestParam) {
        // 使用切面提供的辅助方法获取当前用户ID
        String userId = JwtAuthenticationAspect.getCurrentUserId();
        
        // 使用用户ID进行业务操作
        return Result.success("操作成功，用户ID: " + userId);
    }
}

测试方法

使用Postman测试：
a. 首先发送登录请求获取JWT令牌：（现在的登录请求Result已经设计成带data的了，里面可以拿到jwt令牌）
CopyPOST http://localhost:8080/api/user/login
Content-Type: application/json

{
  "username": "user_test_sharding6666",
  "password": "123456"
}
b. 从登录响应中复制JWT令牌
c. 使用令牌访问需要认证的API：
CopyPOST http://localhost:8080/api/example/action
Content-Type: application/json
Authorization: Bearer 你的JWT令牌

{
  // 请求体内容
}