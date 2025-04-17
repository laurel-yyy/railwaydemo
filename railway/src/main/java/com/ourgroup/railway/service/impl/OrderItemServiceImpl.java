package com.ourgroup.railway.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.ourgroup.railway.model.dao.OrderItemDO;
import com.ourgroup.railway.mapper.OrderItemMapper;
import com.ourgroup.railway.mapper.OrderMapper;
import com.ourgroup.railway.model.dto.req.TicketOrderItemQueryReqDTO;
import com.ourgroup.railway.model.dto.resp.TicketOrderPassengerDetailRespDTO;
import com.ourgroup.railway.service.OrderItemService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Order item service implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;

    @Override
    public List<TicketOrderPassengerDetailRespDTO> queryTicketItemOrderById(TicketOrderItemQueryReqDTO requestParam) {
        List<OrderItemDO> orderItemList;
        
        // Check if request has order item IDs
        if (requestParam.getOrderItemRecordIds() != null && !requestParam.getOrderItemRecordIds().isEmpty()) {
            // Query by specific order item IDs
            orderItemList = new ArrayList<>();
            for (Long itemId : requestParam.getOrderItemRecordIds()) {
                OrderItemDO orderItem = orderItemMapper.findById(itemId);
                if (orderItem != null) {
                    orderItemList.add(orderItem);
                }
            }
        } else if (requestParam.getOrderSn() != null && !requestParam.getOrderSn().isEmpty()) {
            // Query by order number
            orderItemList = orderItemMapper.findByOrderSn(requestParam.getOrderSn());
        } else {
            // No valid parameters provided
            return Collections.emptyList();
        }
        
        // Convert order items to passenger details
        return convertToPassengerDetailList(orderItemList);
    }
    
    /**
     * Convert OrderItemDO list to TicketOrderPassengerDetailRespDTO list
     */
    private List<TicketOrderPassengerDetailRespDTO> convertToPassengerDetailList(List<OrderItemDO> orderItems) {
        List<TicketOrderPassengerDetailRespDTO> result = new ArrayList<>();
        
        for (OrderItemDO orderItem : orderItems) {
            TicketOrderPassengerDetailRespDTO dto = TicketOrderPassengerDetailRespDTO.builder()
                .id(orderItem.getId())
                .userId(orderItem.getUserId())
                .username(orderItem.getUsername())
                .realName(orderItem.getRealName())
                .idType(orderItem.getIdType())
                .idCard(orderItem.getIdCard())
                // .phone(orderItem.getPhone())
                .seatType(orderItem.getSeatType())
                .carriageNumber(orderItem.getCarriageNumber())
                .seatNumber(orderItem.getSeatNumber())
                .amount(orderItem.getAmount())
                .ticketType(orderItem.getTicketType())
                .status(orderItem.getStatus())
                .build();
            
            // Set status name based on status code
            // In a real implementation, you might have an enum mapping
            switch (orderItem.getStatus()) {
                case 0:
                    dto.setStatusName("Normal");
                    break;
                case 1:
                    dto.setStatusName("Used");
                    break;
                case 2:
                    dto.setStatusName("Closed");
                    break;
                default:
                    dto.setStatusName("Unknown");
            }
            
            result.add(dto);
        }
        
        return result;
    }
}