package com.ourgroup.railway.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourgroup.railway.model.dao.OrderDO;
import com.ourgroup.railway.mapper.OrderMapper;
import com.ourgroup.railway.model.dto.req.ChangeTicketOrderReqDTO;
import com.ourgroup.railway.model.dto.req.TicketOrderCreateReqDTO;
import com.ourgroup.railway.model.dto.req.TicketOrderPageQueryReqDTO;
import com.ourgroup.railway.model.dto.req.TicketOrderSelfPageQueryReqDTO;
import com.ourgroup.railway.model.dto.resp.TicketOrderDetailRespDTO;
import com.ourgroup.railway.model.dto.resp.TicketOrderDetailSelfRespDTO;
import com.ourgroup.railway.service.OrderService;
import com.ourgroup.railway.framework.convention.page.PageResponse;
import com.ourgroup.railway.framework.toolkit.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Order service implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    private final HttpServletRequest request;

    @Override
    public TicketOrderDetailRespDTO queryTicketOrderByOrderSn(String orderSn) {

        OrderDO orderDO = orderMapper.findByOrderSn(orderSn);
        if (orderDO == null) {
            return null;
        }

        TicketOrderDetailRespDTO result = convertToOrderDetailDTO(orderDO);
        
        return result;
    }

    @Override
    public PageResponse<TicketOrderDetailRespDTO> pageTicketOrder(TicketOrderPageQueryReqDTO requestParam) {
        
        String authHeader = request.getHeader("Authorization");
        String userId1 = null;
    
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        userId1 = JWTUtil.getUserIdFromToken(token);
    }

        List<OrderDO> orderList = orderMapper.findByUserId(Long.parseLong(userId1));
        List<TicketOrderDetailRespDTO> resultList = new ArrayList<>();
        
        for (OrderDO orderDO : orderList) {
            TicketOrderDetailRespDTO detailDTO = convertToOrderDetailDTO(orderDO);
            resultList.add(detailDTO);
        }
        
        // Create page response
        return PageResponse.of(
                resultList,
                requestParam.getCurrent(),
                requestParam.getSize(),
                (long) resultList.size()
        );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String createTicketOrder(TicketOrderCreateReqDTO requestParam) {
        // Generate order number
        String orderSn = generateOrderSn();
        Date now = new Date();
        
        // Check before get userid
        if (requestParam.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null for sharding");
        }
            
        // Create order
        OrderDO orderDO = new OrderDO();
        orderDO.setOrderSn(orderSn);
        orderDO.setUserId(requestParam.getUserId());
        orderDO.setUsername(requestParam.getUsername());
        orderDO.setTrainId(requestParam.getTrainId());
        orderDO.setTrainNumber(requestParam.getTrainNumber());
        orderDO.setDeparture(requestParam.getDeparture());
        orderDO.setArrival(requestParam.getArrival());
        orderDO.setStatus(0); // Pending payment
        orderDO.setOrderTime(now);
        orderDO.setRidingDate(requestParam.getRidingDate());
        orderDO.setDepartureTime(requestParam.getDepartureTime());
        orderDO.setArrivalTime(requestParam.getArrivalTime());
        orderDO.setCreateTime(now);
        orderDO.setUpdateTime(now);
        
        orderMapper.insert(orderDO);
                
        return orderSn;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean payTickOrder(ChangeTicketOrderReqDTO requestParam) {
        String orderSn = requestParam.getOrderSn();
        
        // Find order
        OrderDO orderDO = orderMapper.findByOrderSn(orderSn);
        if (orderDO == null || orderDO.getStatus() != 0) { // 0 = Pending payment
            return false;
        }
        
        // Update order status
        orderDO.setStatus(2); // Closed
        orderDO.setUpdateTime(new Date());
        // Update order status
        int updated = orderMapper.updateStatusByOrderSn(orderSn, 2); 
        
        if (updated <= 0) {
            return false;
        }
        
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean cancelTickOrder(ChangeTicketOrderReqDTO requestParam) {
        return payTickOrder(requestParam);
    }

    @Override
    public PageResponse<TicketOrderDetailSelfRespDTO> pageSelfTicketOrder(TicketOrderSelfPageQueryReqDTO requestParam) {
        
        return PageResponse.empty();
    }

    // Utility methods
    
    private String generateOrderSn() {
        return "RW" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
    }
    
    private TicketOrderDetailRespDTO convertToOrderDetailDTO(OrderDO orderDO) {
        TicketOrderDetailRespDTO dto = new TicketOrderDetailRespDTO();
        dto.setId(orderDO.getId());
        dto.setOrderSn(orderDO.getOrderSn());
        dto.setUserId(orderDO.getUserId());
        dto.setUsername(orderDO.getUsername());
        dto.setTrainId(orderDO.getTrainId());
        dto.setTrainNumber(orderDO.getTrainNumber());
        dto.setDeparture(orderDO.getDeparture());
        dto.setArrival(orderDO.getArrival());
        dto.setStatus(orderDO.getStatus());
        dto.setOrderTime(orderDO.getOrderTime());
        dto.setPayTime(orderDO.getPayTime());
        dto.setRidingDate(orderDO.getRidingDate());
        dto.setDepartureTime(orderDO.getDepartureTime());
        dto.setArrivalTime(orderDO.getArrivalTime());
        dto.setCreateTime(orderDO.getCreateTime());
        dto.setUpdateTime(orderDO.getUpdateTime());
        
        return dto;
    }
    
  
}