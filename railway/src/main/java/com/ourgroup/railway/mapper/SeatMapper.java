package com.ourgroup.railway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ourgroup.railway.model.dao.SeatDO;

@Mapper
public interface SeatMapper { 

        /**
         * 统计符合条件的可用座位数量
         *
         * @param trainId 列车ID
         * @param seatType 座位类型
         * @param seatStatus 座位状态
         * @param startStation 出发站
         * @param endStation 到达站
         * @return 可用座位数量
         */
        @Select("SELECT COUNT(*) FROM t_seat " +
                "WHERE train_id = #{trainId} " +
                "AND seat_type = #{seatType} " +
                "AND seat_status = #{seatStatus} " +
                "AND start_station = #{startStation} " +
                "AND end_station = #{endStation} " +
                "AND del_flag = 0")

        Integer countAvailableSeats(
                @Param("trainId") Long trainId,
                @Param("seatType") Integer seatType,
                @Param("seatStatus") Integer seatStatus,
                @Param("startStation") String startStation,
                @Param("endStation") String endStation);

        
        @Select("SELECT * FROM t_seat " +
                "WHERE train_id = #{trainId} " +
                "AND carriage_number = #{carriageNumber} " +
                "AND seat_type = #{seatType} " +
                "AND start_station = #{departure} " +
                "AND end_station = #{arrival} " +
                "AND seat_status = #{seatStatus} " +
                "AND del_flag = 0")
        List<SeatDO> selectAvailableSeatList(@Param("trainId")Long trainId, @Param("carriageNumber")String carriageNumber, @Param("seatType")Integer seatType, 
                @Param("departure")String departure, @Param("arrival")String arrival, @Param("seatStatus")Integer seatStatus);


        List<Integer> listSeatRemainingTicket(SeatDO seatDO, List<String> trainCarriageList);


        void batchUpdateSeatStatus(List<SeatDO> batchUpdateList);

        List<String> listUsableCarriageNumber(String trainId, Integer carriageType, String departure, String arrival, Integer seatStatus);
}
