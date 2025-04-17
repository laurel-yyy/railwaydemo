package com.ourgroup.railway.model.dto.resp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainPurchaseTicketRespDTO {

    /**
     * 乘车人 ID
     */
    private String passengerId;

    /**
     * 乘车人姓名
     */
    private String realName;

    
    /**
     * 席别类型
     */
    private Integer seatType;

    /**
     * 车厢号
     */
    private String carriageNumber;

    /**
     * 座位号
     */
    private String seatNumber;

    /**
     * 座位金额
     */
    private Integer amount;
}
