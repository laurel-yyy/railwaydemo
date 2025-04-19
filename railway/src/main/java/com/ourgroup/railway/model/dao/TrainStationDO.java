package com.ourgroup.railway.model.dao;
import lombok.Data;
import com.ourgroup.railway.model.dao.BaseDO;

import java.util.Date;

@Data
public class TrainStationDO extends BaseDO {

    private Long id;

    private Long trainId;

    private Long stationId;

    private String sequence;

    private String departure;

    private String arrival;

    private Date arrivalTime;

    private Date departureTime;

    private Integer stopoverTime;
}
