package com.ourgroup.railway.model.dto.domain;

import lombok.Data;

import java.util.List;

@Data
public class TicketListDTO {

    /**
     * 列车 ID
     */
    private String trainId;

    /**
     * 车次
     */
    private String trainNumber;

    /**
     * 出发时间
     */
    private String departureTime;

    /**
     * 到达时间
     */
    private String arrivalTime;

    /**
     * 历时
     */
    private String duration;

    /**
     * 到达天数
     */
    private Integer daysArrived;

    /**
     * 出发站点
     */
    private String departure;

    /**
     * 到达站点
     */
    private String arrival;

    /**
     * 始发站标识
     */
    private Boolean departureFlag;

    /**
     * 终点站标识
     */
    private Boolean arrivalFlag;

    /**
     * 可售时间
     */
    private String saleTime;

    /**
     * 销售状态 0：可售 1：不可售 2：未知
     */
    private Integer saleStatus;

    /**
     * 席别实体集合
     */
    private List<SeatClassDTO> seatClassList;
}
