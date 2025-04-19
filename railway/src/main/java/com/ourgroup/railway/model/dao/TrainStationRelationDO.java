

package com.ourgroup.railway.model.dao;
import lombok.Data;
import com.ourgroup.railway.model.dao.BaseDO;

import java.util.Date;


@Data
public class TrainStationRelationDO extends BaseDO {

    private Long id;

    private Long trainId;

    private String departure;

    private String arrival;
    
    private Boolean departureFlag;

    private Boolean arrivalFlag;

    private Date departureTime;

    private Date arrivalTime;
}
