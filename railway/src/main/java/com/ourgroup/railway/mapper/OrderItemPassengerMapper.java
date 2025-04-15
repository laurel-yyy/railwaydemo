package com.ourgroup.railway.mapper;

import com.ourgroup.railway.model.dao.OrderItemPassengerDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Order item passenger mapper interface for database operations
 */
@Mapper
public interface OrderItemPassengerMapper {
    
    /**
     * Insert a new order item passenger
     *
     * @param orderItemPassenger the order item passenger to insert
     * @return the number of rows affected
     */
    int insert(OrderItemPassengerDO orderItemPassenger);
    
    /**
     * Batch insert order item passengers
     *
     * @param orderItemPassengers the list of order item passengers to insert
     * @return the number of rows affected
     */
    int batchInsert(List<OrderItemPassengerDO> orderItemPassengers);
    
    /**
     * Update an existing order item passenger
     *
     * @param orderItemPassenger the order item passenger to update
     * @return the number of rows affected
     */
    int update(OrderItemPassengerDO orderItemPassenger);
    
    /**
     * Delete an order item passenger by ID
     *
     * @param id the order item passenger ID
     * @return the number of rows affected
     */
    int deleteById(Long id);
    
    /**
     * Find an order item passenger by ID
     *
     * @param id the order item passenger ID
     * @return the order item passenger or null if not found
     */
    OrderItemPassengerDO findById(Long id);
    
    /**
     * Find order item passengers by order number
     *
     * @param orderSn the order number
     * @return list of order item passengers
     */
    List<OrderItemPassengerDO> findByOrderSn(String orderSn);
    
    /**
     * Find order item passengers by ID card
     *
     * @param idCard the ID card number
     * @return list of order item passengers
     */
    List<OrderItemPassengerDO> findByIdCard(String idCard);
    
    /**
     * Find order item passenger by order number and ID card
     *
     * @param orderSn the order number
     * @param idCard the ID card number
     * @return the order item passenger or null if not found
     */
    OrderItemPassengerDO findByOrderSnAndIdCard(@Param("orderSn") String orderSn, 
                                              @Param("idCard") String idCard);
    
    /**
     * Delete by order number
     *
     * @param orderSn the order number
     * @return the number of rows affected
     */
    int deleteByOrderSn(String orderSn);
    
    /**
     * Count by ID card
     *
     * @param idCard the ID card number
     * @return the count
     */
    int countByIdCard(String idCard);
}