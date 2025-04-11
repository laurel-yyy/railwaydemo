package com.ourgroup.railway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ourgroup.railway.model.dao.TrainStationPriceDO;

@Mapper
public interface TrainStationPriceMapper {

    /**
     * 根据列车ID和出发站点、到达站点查询车票价格
     *
     * @param trainId 列车ID
     * @param departure 出发站点
     * @param arrival 到达站点
     * @return 车票价格列表
     */
    @Select("SELECT * FROM t_train_station_price WHERE train_id = #{trainId} AND departure = #{departure} AND arrival = #{arrival} AND del_flag = 0")
    List<TrainStationPriceDO> selectByTrainAndStations(long trainId, String departure, String arrival);
    
}
