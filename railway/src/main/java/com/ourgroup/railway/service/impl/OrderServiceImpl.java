package com.ourgroup.railway.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourgroup.railway.model.dao.OrderDO;
import com.ourgroup.railway.model.dao.OrderItemDO;
import com.ourgroup.railway.model.dao.OrderItemPassengerDO;
import com.ourgroup.railway.mapper.OrderMapper;
import com.ourgroup.railway.mapper.OrderItemMapper;
import com.ourgroup.railway.model.dto.req.ChangeTicketOrderReqDTO;
import com.ourgroup.railway.model.dto.req.TicketOrderCreateReqDTO;
import com.ourgroup.railway.model.dto.req.TicketOrderItemCreateReqDTO;
import com.ourgroup.railway.model.dto.req.TicketOrderPageQueryReqDTO;
import com.ourgroup.railway.model.dto.req.TicketOrderSelfPageQueryReqDTO;
import com.ourgroup.railway.model.dto.resp.TicketOrderDetailRespDTO;
import com.ourgroup.railway.model.dto.resp.TicketOrderDetailSelfRespDTO;
import com.ourgroup.railway.model.dto.resp.TicketOrderPassengerDetailRespDTO;
import com.ourgroup.railway.service.OrderItemService;
import com.ourgroup.railway.service.OrderPassengerRelationService;
import com.ourgroup.railway.service.OrderService;
import com.google.protobuf.ServiceException;
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
    private final OrderItemMapper orderItemMapper;
    private final OrderItemService orderItemService;
    private final OrderPassengerRelationService orderPassengerRelationService;

    private final HttpServletRequest request;

    @Override
    public TicketOrderDetailRespDTO queryTicketOrderByOrderSn(String orderSn) {
        // Query order by order number
        OrderDO orderDO = orderMapper.findByOrderSn(orderSn);
        if (orderDO == null) {
            return null;
        }
        
        // Query order items
        // List<OrderItemDO> orderItemList = orderItemMapper.findByOrderSn(orderSn);
        
        // Convert to response DTO
        TicketOrderDetailRespDTO result = convertToOrderDetailDTO(orderDO);
        // result.setPassengerDetails(convertToPassengerDetailList(orderItemList));
        
        return result;
    }

    @Override
    public PageResponse<TicketOrderDetailRespDTO> pageTicketOrder(TicketOrderPageQueryReqDTO requestParam) {
        // This would typically use pagination in a real implementation
        // For simplicity, we'll just fetch all orders matching the criteria
        
        // In a real implementation, you would use requestParam.getCurrent() and requestParam.getSize()
        // to implement proper pagination with database LIMIT and OFFSET
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
            // List<OrderItemDO> orderItemList = orderItemMapper.findByOrderSn(orderDO.getOrderSn());
            // detailDTO.setPassengerDetails(convertToPassengerDetailList(orderItemList));
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
        
        // 在创建订单前添加非空检查
        if (requestParam.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null for sharding");
        }

        // 添加日志记录
        log.info("Raw requestParam object: {}", requestParam);
        log.info("UserId from requestParam: {}", requestParam.getUserId());
        log.info("ToString result: {}", requestParam.toString());
        log.info("Creating order with userId: {}", requestParam.getUserId());
    
        
        // Create order
        OrderDO orderDO = new OrderDO();
        orderDO.setOrderSn(orderSn);
        orderDO.setUserId(requestParam.getUserId());
        orderDO.setUsername(requestParam.getUsername());
        orderDO.setTrainId(requestParam.getTrainId());
        orderDO.setTrainNumber(requestParam.getTrainNumber());
        orderDO.setDeparture(requestParam.getDeparture());
        orderDO.setArrival(requestParam.getArrival());
        // orderDO.setSource(requestParam.getSource() != null ? requestParam.getSource() : 1);
        orderDO.setStatus(0); // Pending payment
        orderDO.setOrderTime(now);
        orderDO.setRidingDate(requestParam.getRidingDate());
        orderDO.setDepartureTime(requestParam.getDepartureTime());
        orderDO.setArrivalTime(requestParam.getArrivalTime());
        orderDO.setCreateTime(now);
        orderDO.setUpdateTime(now);
        
        orderMapper.insert(orderDO);
        
        // Create order items and passenger relations
        // List<TicketOrderItemCreateReqDTO> ticketOrderItems = requestParam.getTicketOrderItems();
        // if (ticketOrderItems != null && !ticketOrderItems.isEmpty()) {
        //     for (TicketOrderItemCreateReqDTO item : ticketOrderItems) {
        //         // Create order item
        //         OrderItemDO orderItemDO = new OrderItemDO();
        //         orderItemDO.setOrderSn(orderSn);
        //         orderItemDO.setUserId(requestParam.getUserId());
        //         orderItemDO.setUsername(requestParam.getUsername());
        //         orderItemDO.setTrainId(requestParam.getTrainId());
        //         orderItemDO.setCarriageNumber(item.getCarriageNumber());
        //         orderItemDO.setSeatType(item.getSeatType());
        //         orderItemDO.setSeatNumber(item.getSeatNumber());
        //         orderItemDO.setRealName(item.getRealName());
        //         orderItemDO.setIdType(item.getIdType());
        //         orderItemDO.setIdCard(item.getIdCard());
        //         orderItemDO.setPhone(item.getPhone());
        //         orderItemDO.setStatus(0); // Normal
        //         orderItemDO.setAmount(item.getAmount());
        //         orderItemDO.setTicketType(item.getTicketType());
        //         orderItemDO.setCreateTime(now);
        //         orderItemDO.setUpdateTime(now);
                
        //         orderItemMapper.insert(orderItemDO);
                
        //         // Create passenger relation
        //         OrderItemPassengerDO passengerDO = new OrderItemPassengerDO();
        //         passengerDO.setOrderSn(orderSn);
        //         passengerDO.setIdType(item.getIdType());
        //         passengerDO.setIdCard(item.getIdCard());
        //         passengerDO.setCreateTime(now);
        //         passengerDO.setUpdateTime(now);
                
        //         // In a real implementation, you would save the passenger relation
        //         // For now, we'll leave this as a placeholder since we didn't implement the repo method
        //     }
        // }
        
        // In a real implementation, you might want to send a delayed message to close the order
        // if it's not paid within a certain timeframe
        
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
        int updated = orderMapper.updateStatusByOrderSn(orderSn, 2); // 使用这个方法代替 update
        
        if (updated <= 0) {
            return false;
        }
        
        // Update order items status
        // List<OrderItemDO> orderItems = orderItemMapper.findByOrderSn(orderSn);
        // for (OrderItemDO orderItem : orderItems) {
        //     orderItem.setStatus(2); // Closed
        //     orderItem.setUpdateTime(new Date());
        //     orderItemMapper.update(orderItem);
        // }
        
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean cancelTickOrder(ChangeTicketOrderReqDTO requestParam) {
        // The logic is similar to payTickOrder in this simplified implementation
        return payTickOrder(requestParam);
    }

    @Override
    public PageResponse<TicketOrderDetailSelfRespDTO> pageSelfTicketOrder(TicketOrderSelfPageQueryReqDTO requestParam) {
        // In a real implementation, this would query orders for the current user
        // For now, we'll just return an empty page
        
        return PageResponse.empty();
    }

    // Utility methods
    
    private String generateOrderSn() {
        // In a real implementation, you would use a more sophisticated order number generation strategy
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
        // dto.setSource(orderDO.getSource());
        dto.setStatus(orderDO.getStatus());
        dto.setOrderTime(orderDO.getOrderTime());
        // dto.setPayType(orderDO.getPayType());
        dto.setPayTime(orderDO.getPayTime());
        dto.setRidingDate(orderDO.getRidingDate());
        dto.setDepartureTime(orderDO.getDepartureTime());
        dto.setArrivalTime(orderDO.getArrivalTime());
        dto.setCreateTime(orderDO.getCreateTime());
        dto.setUpdateTime(orderDO.getUpdateTime());
        
        return dto;
    }
    
    // private List<TicketOrderPassengerDetailRespDTO> convertToPassengerDetailList(List<OrderItemDO> orderItems) {
    //     List<TicketOrderPassengerDetailRespDTO> result = new ArrayList<>();
        
    //     for (OrderItemDO orderItem : orderItems) {
    //         TicketOrderPassengerDetailRespDTO dto = new TicketOrderPassengerDetailRespDTO();
    //         dto.setId(orderItem.getId());
    //         dto.setRealName(orderItem.getRealName());
    //         dto.setIdType(orderItem.getIdType());
    //         dto.setIdCard(orderItem.getIdCard());
    //         // dto.setPhone(orderItem.getPhone());
    //         dto.setSeatType(orderItem.getSeatType());
    //         dto.setCarriageNumber(orderItem.getCarriageNumber());
    //         dto.setSeatNumber(orderItem.getSeatNumber());
    //         dto.setAmount(orderItem.getAmount());
    //         dto.setTicketType(orderItem.getTicketType());
    //         dto.setStatus(orderItem.getStatus());
            
    //         result.add(dto);
    //     }
        
    //     return result;
    // }
}