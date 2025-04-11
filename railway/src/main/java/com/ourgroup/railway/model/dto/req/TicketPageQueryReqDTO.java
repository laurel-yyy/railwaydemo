package com.ourgroup.railway.model.dto.req;

import lombok.Data;
import com.ourgroup.railway.framework.page.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TicketPageQueryReqDTO extends PageRequest {

    /**
     * 出发地 Code
     */
    private String fromStation;

    /**
     * 目的地 Code
     */
    private String toStation;

    /**
     * 出发日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date departureDate;

    /**
     * 出发站点
     */
    private String departure;

    /**
     * 到达站点
     */
    private String arrival;
}
