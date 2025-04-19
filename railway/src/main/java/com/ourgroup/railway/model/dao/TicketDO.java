package com.ourgroup.railway.model.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ourgroup.railway.model.dao.BaseDO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDO extends BaseDO {


    private Long id;

    private String username;


    private Long trainId;


    private String carriageNumber;

    private String seatNumber;

    private String passengerId;

    private Integer ticketStatus;
}
