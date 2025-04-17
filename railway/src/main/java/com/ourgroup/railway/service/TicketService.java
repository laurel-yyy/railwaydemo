package com.ourgroup.railway.service;

import com.google.protobuf.ServiceException;
import com.ourgroup.railway.model.dto.req.PurchaseTicketReqDTO;
import com.ourgroup.railway.model.dto.req.TicketPageQueryReqDTO;
import com.ourgroup.railway.model.dto.resp.TicketPageQueryRespDTO;
import com.ourgroup.railway.model.dto.resp.TicketPurchaseRespDTO;

public interface TicketService {
    TicketPageQueryRespDTO pageListQueryTicket(TicketPageQueryReqDTO requestParam);

    TicketPurchaseRespDTO purchaseTicket(PurchaseTicketReqDTO requestParam) throws ServiceException;

    TicketPurchaseRespDTO executePurchaseTickets(PurchaseTicketReqDTO requestParam) throws ServiceException;
} 