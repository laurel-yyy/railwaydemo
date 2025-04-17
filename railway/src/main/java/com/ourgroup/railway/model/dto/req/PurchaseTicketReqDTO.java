package com.ourgroup.railway.model.dto.req;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseTicketReqDTO {

    /**
     * 车次 ID
     */
    private String trainId;

    /**
     * 乘车人 ID
     */
    private String passengerId;

    /**
     * 座位类型
     */
    private Integer seatType;

    /**
     * 选择座位
     */
    private String chooseSeat;

    /**
     * 出发站点
     */
    private String departure;

    /**
     * 到达站点
     */
    private String arrival;

}
