package com.ourgroup.railway.model.dto.req;

import lombok.Data;
import com.ourgroup.railway.framework.convention.page.PageRequest;

/**
 * Ticket order page query request DTO
 */
@Data
public class TicketOrderPageQueryReqDTO extends PageRequest {

    /**
     * user id
     */
    private String userId;

    /**
     * status type 0: unfinished 1: upcoming trip 2: historical order
     */
    private Integer statusType;
}