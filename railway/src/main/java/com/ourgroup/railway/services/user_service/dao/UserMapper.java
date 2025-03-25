package com.ourgroup.railway.services.user_service.dao;  // package path

import com.baomidou.mybatisplus.core.mapper.BaseMapper;  // MyBatis-Plus provided BaseMapper interface for CRUD operations
import org.apache.ibatis.annotations.Mapper;            // MyBatis provided Mapper annotation for marking mapper interfaces
import org.springframework.stereotype.Repository;        
import com.ourgroup.railway.services.user_service.dao.UserDO; // import the UserDO class

@Mapper  // MyBatis annotation to indicate this is a mapper interface

public interface UserMapper extends BaseMapper<UserDO> {
    // This interface extends BaseMapper, which provides basic CRUD operations for UserDO.
    
}
