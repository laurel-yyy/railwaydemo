package com.ourgroup.railway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TrainStationMapper {

    /**
     * 查询指定列车的所有站点
     *
     * @param trainId 列车ID
     * @return 站点列表
     */
    @Select("SELECT departure FROM t_train_station WHERE train_id = #{trainId} AND del_flag = 0")
    List<String> selectStationsByTrainId(@Param("trainId") Long trainId);    
    
}
