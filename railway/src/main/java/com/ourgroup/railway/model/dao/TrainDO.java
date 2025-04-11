package com.ourgroup.railway.model.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ourgroup.railway.model.dao.BaseDO;

import java.util.Date;


@Data
public class TrainDO extends BaseDO {


    private Long id;


    private String trainNumber;


    private Integer trainType;


    private String trainTag;

    private String trainBrand;

    private String startStation;


    private String endStation;

    private Date saleTime;

    /**
     * 销售状态 0：可售 1：不可售 2：未知
     */
    private Integer saleStatus;

    private Date departureTime;


    private Date arrivalTime;
}
