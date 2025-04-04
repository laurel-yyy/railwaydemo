package com.ourgroup.railway.mapper;

import com.ourgroup.railway.model.dao.UserDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM t_user WHERE id = #{id}")
    UserDO selectById(Long id);

    @Select("SELECT * FROM t_user LIMIT 1000") // 添加限制以避免全表扫描返回过多数据
    List<UserDO> selectAll();

    @Insert("INSERT INTO t_user (id, username, password, real_name) VALUES (#{id}, #{username}, #{password}, #{realName})")
    int insert(UserDO user);

    @Select("SELECT * FROM t_user WHERE username = #{username} LIMIT 1")
    List<UserDO> findByUsername(String username);
    
    @Select("SELECT * FROM t_user WHERE username = #{username} AND password = #{password} LIMIT 1")
    List<UserDO> findByUsernameAndPassword(String username, String password);
}