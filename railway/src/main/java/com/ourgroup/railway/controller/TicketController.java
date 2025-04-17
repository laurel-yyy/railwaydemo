package com.ourgroup.railway.controller;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.google.protobuf.ServiceException;
import com.ourgroup.railway.framework.result.Result;
import com.ourgroup.railway.framework.result.Results;
import com.ourgroup.railway.model.dto.req.PurchaseTicketReqDTO;
import com.ourgroup.railway.model.dto.req.TicketPageQueryReqDTO;
import com.ourgroup.railway.model.dto.resp.TicketPageQueryRespDTO;
import com.ourgroup.railway.model.dto.resp.TicketPurchaseRespDTO;
import com.ourgroup.railway.service.TicketService;
import lombok.extern.slf4j.Slf4j;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    @Autowired
    @Lazy
    private final TicketService ticketService;

    @GetMapping("/api/ticket/query")
    public Result<TicketPageQueryRespDTO> pageListQueryTicket(TicketPageQueryReqDTO requestParam) {
        TicketPageQueryRespDTO ticketPageQuery = ticketService.pageListQueryTicket(requestParam);
        return Results.success(ticketPageQuery);
    }

    @PostMapping("/api/ticket/purchase")
    public Result<TicketPurchaseRespDTO> purchaseTicket(@RequestBody PurchaseTicketReqDTO requestParam) {
        log.info("Received ticket purchase request: {}", requestParam);
        try {
            TicketPurchaseRespDTO ticketPurchase = ticketService.purchaseTicket(requestParam);
            return Results.success(ticketPurchase);
        } catch (ServiceException e) {
            // Handle the exception (e.g., log it or return an error response)
            return Results.failure("Error purchasing ticket: " + e.getMessage());
        }
    }

}
