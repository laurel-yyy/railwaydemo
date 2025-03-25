package com.ourgroup.railway.services.user_service.dao;

import com.baomidou.mybatisplus.annotation.TableId; // MyBatis-Plus annotation for primary key
import com.baomidou.mybatisplus.annotation.TableName; // MyBatis-Plus annotation for table name
import com.baomidou.mybatisplus.annotation.IdType; // MyBatis-Plus annotation for ID generation strategy
import lombok.Data; // Lombok annotation to generate getters, setters, and other methods

@Data
@TableName("t_user")  // Logical table name, ShardingSphere will map to the actual sharded tables like t_user0, t_user1, etc.
public class UserDO {
    
    @TableId(type = IdType.AUTO)  // Auto-increment ID
    private Long id;

    private String username;  // Username

    private String password;  // Password

    private String realName;  // Real name
}
