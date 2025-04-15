package com.ourgroup.railway.service.impl;

import org.springframework.stereotype.Service;

import com.ourgroup.railway.model.dao.OrderItemPassengerDO;
import com.ourgroup.railway.mapper.OrderItemPassengerMapper;
import com.ourgroup.railway.service.OrderPassengerRelationService;

/**
 * Order passenger relation service implementation
 */
@Service
public class OrderPassengerRelationServiceImpl implements OrderPassengerRelationService {
    
    private final OrderItemPassengerMapper orderItemPassengerMapper;
    
    public OrderPassengerRelationServiceImpl(OrderItemPassengerMapper orderItemPassengerMapper) {
        this.orderItemPassengerMapper = orderItemPassengerMapper;
    }
}