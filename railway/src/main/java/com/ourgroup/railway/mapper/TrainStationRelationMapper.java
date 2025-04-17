package com.ourgroup.railway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ourgroup.railway.model.dao.TrainStationRelationDO;

@Mapper
public interface TrainStationRelationMapper {

    @Select("SELECT * FROM t_train_station_relation " +
            "WHERE departure = #{departure} AND arrival = #{arrival} AND del_flag = 0")
    List<TrainStationRelationDO> selectByDepartureAndArrival(@Param("departure") String departure,  @Param("arrival") String arrival);
    
    /**
     * Get all station relations for a specific train
     *
     * @param trainId Train ID
     * @return List of station relations
     */
    @Select("SELECT * FROM t_train_station_relation WHERE train_id = #{trainId} AND del_flag = 0")
    List<TrainStationRelationDO> selectByTrainId(@Param("trainId") Long trainId);
}

