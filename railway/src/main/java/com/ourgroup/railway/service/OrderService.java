package com.ourgroup.railway.service;

import com.ourgroup.railway.model.dto.req.ChangeTicketOrderReqDTO;
import com.ourgroup.railway.model.dto.req.TicketOrderCreateReqDTO;
import com.ourgroup.railway.model.dto.req.TicketOrderPageQueryReqDTO;
import com.ourgroup.railway.model.dto.req.TicketOrderSelfPageQueryReqDTO;
import com.ourgroup.railway.model.dto.resp.TicketOrderDetailRespDTO;
import com.ourgroup.railway.model.dto.resp.TicketOrderDetailSelfRespDTO;
import com.google.protobuf.ServiceException;
import com.ourgroup.railway.framework.convention.page.PageResponse;

/**
 * Order service interface
 */
public interface OrderService {
    /**
     * Query ticket order by order number
     *
     * @param orderSn order number
     * @return order details
     */
    TicketOrderDetailRespDTO queryTicketOrderByOrderSn(String orderSn);

    /**
     * Page query ticket orders
     *
     * @param requestParam page query parameters
     * @return paged order details
     * @throws ServiceException 
     */
    PageResponse<TicketOrderDetailRespDTO> pageTicketOrder(TicketOrderPageQueryReqDTO requestParam);

    /**
     * Create ticket order
     *
     * @param requestParam order creation parameters
     * @return order number
     */
    String createTicketOrder(TicketOrderCreateReqDTO requestParam);

    /**
     * Close ticket order
     *
     * @param requestParam order closing parameters
     * @return operation result
     */
    boolean payTickOrder(ChangeTicketOrderReqDTO requestParam);

    /**
     * Cancel ticket order
     *
     * @param requestParam order cancellation parameters
     * @return operation result
     */
    boolean cancelTickOrder(ChangeTicketOrderReqDTO requestParam);

    // /**
    //  * Query self ticket orders
    //  *
    //  * @param requestParam page query parameters
    //  * @return paged self order details
    //  */
    // PageResponse<TicketOrderDetailSelfRespDTO> pageSelfTicketOrder(TicketOrderSelfPageQueryReqDTO requestParam);
}