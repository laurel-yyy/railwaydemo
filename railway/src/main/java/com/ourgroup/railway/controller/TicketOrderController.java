package com.ourgroup.railway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ourgroup.railway.model.dto.req.ChangeTicketOrderReqDTO;
import com.ourgroup.railway.model.dto.req.TicketOrderCreateReqDTO;
import com.ourgroup.railway.model.dto.req.TicketOrderItemQueryReqDTO;
import com.ourgroup.railway.model.dto.req.TicketOrderPageQueryReqDTO;
import com.ourgroup.railway.model.dto.req.TicketOrderSelfPageQueryReqDTO;
import com.ourgroup.railway.model.dto.resp.TicketOrderDetailRespDTO;
import com.ourgroup.railway.model.dto.resp.TicketOrderDetailSelfRespDTO;
import com.ourgroup.railway.model.dto.resp.TicketOrderPassengerDetailRespDTO;
import com.ourgroup.railway.service.OrderService;
import com.ourgroup.railway.framework.convention.page.PageResponse;
import com.ourgroup.railway.framework.result.Result;
import com.ourgroup.railway.framework.result.Results;

import java.util.List;

/**
 * Ticket order API controller
 */
@RestController
@RequiredArgsConstructor
public class TicketOrderController {

    private final OrderService orderService;

    /**
     * Page query ticket orders
     */
    @GetMapping("/api/order-service/order/ticket/page")
    public Result<PageResponse<TicketOrderDetailRespDTO>> pageTicketOrder(TicketOrderPageQueryReqDTO requestParam) {
        return Results.success(orderService.pageTicketOrder(requestParam));
    }


    /**
     * Create ticket order
     */
    @PostMapping("/api/order-service/order/ticket/create")
    public Result<String> createTicketOrder(@RequestBody TicketOrderCreateReqDTO requestParam) {
        return Results.success(orderService.createTicketOrder(requestParam));
    }

    /**
     * Close ticket order
     */
    @PostMapping("/api/order-service/order/ticket/pay")
    public Result<Boolean> payTickOrder(ChangeTicketOrderReqDTO requestParam) {
        return Results.success(orderService.payTickOrder(requestParam));
    }

    /**
     * Cancel ticket order
     */
    @PostMapping("/api/order-service/order/ticket/cancel")
    public Result<Boolean> cancelTickOrder(ChangeTicketOrderReqDTO requestParam) {
        return Results.success(orderService.cancelTickOrder(requestParam));
    }
}