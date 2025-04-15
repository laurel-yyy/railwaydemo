package com.ourgroup.railway.model.dto.req;

import lombok.Data;
import java.util.List;

/**
 * Ticket order item query request DTO
 */
@Data
public class TicketOrderItemQueryReqDTO {

    /**
     * order number
     */
    private String orderSn;

    /**
     * order item record ids
     */
    private List<Long> orderItemRecordIds;
}