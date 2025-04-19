package com.ourgroup.railway.model.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ourgroup.railway.model.dao.BaseDO;


@Data
@Builder
public class SeatDO extends BaseDO {

    private Long id;

    private Long trainId;

    private String carriageNumber;

    private String seatNumber;

    private Integer seatType;

    private String startStation;

    private String endStation;

    private Integer seatStatus;

    private Integer price;
}
