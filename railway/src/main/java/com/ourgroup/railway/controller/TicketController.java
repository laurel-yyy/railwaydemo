package com.ourgroup.railway.controller;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourgroup.railway.framework.result.Result;
import com.ourgroup.railway.model.dto.req.PurchaseTicketReqDTO;
import com.ourgroup.railway.model.dto.req.TicketPageQueryReqDTO;
import com.ourgroup.railway.model.dto.resp.TicketPageQueryRespDTO;
import com.ourgroup.railway.model.dto.resp.TicketPurchaseRespDTO;
import com.ourgroup.railway.service.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TicketController {

    @Autowired
    @Lazy
    private final TicketService ticketService;

    @GetMapping("/api/ticket/query")
    public Result<TicketPageQueryRespDTO> pageListQueryTicket(TicketPageQueryReqDTO requestParam) {
        TicketPageQueryRespDTO ticketPageQuery = ticketService.pageListQueryTicket(requestParam);
        return Result.success(ticketPageQuery);
    }

    @PostMapping("/api/ticket/purchase")
    public Result<TicketPurchaseRespDTO> purchaseTicket(PurchaseTicketReqDTO requestParam) {
        TicketPurchaseRespDTO ticketPurchase = ticketService.purchaseTicket(requestParam);
        return Result.success(ticketPurchase);
    }

}
