package com.ourgroup.railway.model.dto.req;

import lombok.Data;
import com.ourgroup.railway.framework.convention.page.PageRequest;

/**
 * Self ticket order page query request
 */
@Data
public class TicketOrderSelfPageQueryReqDTO extends PageRequest {
    
    /**
     * user id
     */
    private Long userId;
}