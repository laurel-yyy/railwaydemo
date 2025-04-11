package com.ourgroup.railway.model.dao;

import lombok.Builder;
import lombok.Data;
import com.ourgroup.railway.model.dao.BaseDO;


@Data
@Builder
public class CarriageDO extends BaseDO {

    private Long id;

    private Long trainId;


    private String carriageNumber;

    private Integer carriageType;

    private Integer seatCount;
}
