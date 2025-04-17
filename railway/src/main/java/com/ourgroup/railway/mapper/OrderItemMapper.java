package com.ourgroup.railway.mapper;

import com.ourgroup.railway.model.dao.OrderItemDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Order item mapper interface for database operations
 */
@Mapper
public interface OrderItemMapper {
    
    /**
     * Insert a new order item
     *
     * @param orderItem the order item to insert
     * @return the number of rows affected
     */
    int insert(OrderItemDO orderItem);
    
    /**
     * Batch insert order items
     *
     * @param orderItems the list of order items to insert
     * @return the number of rows affected
     */
    int batchInsert(List<OrderItemDO> orderItems);
    
    /**
     * Update an existing order item
     *
     * @param orderItem the order item to update
     * @return the number of rows affected
     */
    int update(OrderItemDO orderItem);
    
    /**
     * Delete an order item by ID
     *
     * @param id the order item ID
     * @return the number of rows affected
     */
    int deleteById(Long id);
    
    /**
     * Find an order item by ID
     *
     * @param id the order item ID
     * @return the order item or null if not found
     */
    OrderItemDO findById(Long id);
    
    /**
     * Find order items by order number
     *
     * @param orderSn the order number
     * @return list of order items
     */
    List<OrderItemDO> findByOrderSn(String orderSn);
    
    /**
     * Find order items by user ID
     *
     * @param userId the user ID
     * @return list of order items
     */
    List<OrderItemDO> findByUserId(Long userId);
    
    /**
     * Find order items by train ID
     *
     * @param trainId the train ID
     * @return list of order items
     */
    List<OrderItemDO> findByTrainId(Long trainId);
    
    /**
     * Find order items by seat information
     *
     * @param trainId the train ID
     * @param carriageNumber the carriage number
     * @param seatNumber the seat number
     * @return list of order items
     */
    List<OrderItemDO> findBySeatInfo(@Param("trainId") Long trainId, 
                                    @Param("carriageNumber") String carriageNumber,
                                    @Param("seatNumber") String seatNumber);
    
    /**
     * Update status by order number
     *
     * @param orderSn the order number
     * @param status the new status
     * @return the number of rows affected
     */
    int updateStatusByOrderSn(@Param("orderSn") String orderSn, @Param("status") Integer status);
    
    /**
     * Find order items by ID card
     *
     * @param idCard the ID card number
     * @return list of order items
     */
    List<OrderItemDO> findByIdCard(String idCard);
    
    /**
     * Count order items by order number
     *
     * @param orderSn the order number
     * @return the count
     */
    int countByOrderSn(String orderSn);
}