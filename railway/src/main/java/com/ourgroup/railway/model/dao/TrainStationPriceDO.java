package com.ourgroup.railway.model.dao;

import lombok.Data;
import com.ourgroup.railway.model.dao.BaseDO;
import java.util.Date;

@Data
public class TrainStationPriceDO extends BaseDO {

    private Long id;

    private Long trainId;

    private Integer seatType;

    private String departure;

    private String arrival;

    private Integer price;
}
