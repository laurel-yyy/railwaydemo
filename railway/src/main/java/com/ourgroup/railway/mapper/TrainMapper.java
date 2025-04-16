package com.ourgroup.railway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ourgroup.railway.model.dao.TrainDO;

@Mapper
public interface TrainMapper {

    /**
     * 根据ID查询列车信息
     *
     * @param id 列车ID
     * @return 列车信息
     */
    @Select("SELECT * FROM t_train WHERE id = #{id} AND del_flag = 0")
    TrainDO selectById(@Param("id") Long id);
    
    /**
     * 查询所有未被逻辑删除的列车信息
     *
     * @return 列车列表
     */
    @Select("SELECT * FROM t_train WHERE del_flag = 0")
    List<TrainDO> selectAllTrains();

}
