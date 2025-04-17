package com.ourgroup.railway.service;

import com.ourgroup.railway.model.dao.OrderItemDO;
import com.ourgroup.railway.model.dto.req.TicketOrderItemQueryReqDTO;
import com.ourgroup.railway.model.dto.resp.TicketOrderPassengerDetailRespDTO;

import java.util.List;

/**
 * Order item service interface
 */
public interface OrderItemService {
    
    /**
     * Query ticket item details by order item record ID
     *
     * @param requestParam request parameters
     * @return list of passenger ticket details
     */
    List<TicketOrderPassengerDetailRespDTO> queryTicketItemOrderById(TicketOrderItemQueryReqDTO requestParam);
}