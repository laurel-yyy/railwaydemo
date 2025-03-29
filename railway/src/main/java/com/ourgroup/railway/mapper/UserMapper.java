package com.ourgroup.railway.mapper;

import com.ourgroup.railway.model.dao.UserDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM t_user WHERE id = #{id}")
    UserDO selectById(Long id);

    @Select("SELECT * FROM t_user")
    List<UserDO> selectAll();

    @Insert("INSERT INTO t_user (username, password, real_name) VALUES (#{username}, #{password}, #{realName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserDO user);

    @Select("SELECT * FROM t_user WHERE username = #{username}")
    List<UserDO> findByUsername(String username);
    
    @Select("SELECT * FROM t_user WHERE username = #{username} AND password = #{password}")
    List<UserDO> findByUsernameAndPassword(String username, String password);

}