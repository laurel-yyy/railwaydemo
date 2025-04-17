package com.ourgroup.railway.mapper;

import com.ourgroup.railway.model.dao.OrderDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Order mapper interface for database operations
 */
@Mapper
public interface OrderMapper {
    
    /**
     * Insert a new order
     *
     * @param order the order to insert
     * @return the number of rows affected
     */
    int insert(OrderDO order);
    
    /**
     * Update an existing order
     *
     * @param order the order to update
     * @return the number of rows affected
     */
    int update(OrderDO order);
    
    /**
     * Delete an order by ID
     *
     * @param id the order ID
     * @return the number of rows affected
     */
    int deleteById(Long id);
    
    /**
     * Find an order by ID
     *
     * @param id the order ID
     * @return the order or null if not found
     */
    OrderDO findById(Long id);
    
    /**
     * Find an order by order number
     *
     * @param orderSn the order number
     * @return the order or null if not found
     */
    OrderDO findByOrderSn(String orderSn);
    
    /**
     * Find orders by user ID
     *
     * @param userId the user ID
     * @return list of orders
     */
    List<OrderDO> findByUserId(String userId);
    
    /**
     * Find orders by status
     *
     * @param status the order status
     * @return list of orders
     */
    List<OrderDO> findByStatus(Integer status);
    
    /**
     * Find orders by train number and riding date
     *
     * @param trainNumber the train number
     * @param ridingDate the riding date
     * @return list of orders
     */
    List<OrderDO> findByTrainNumberAndRidingDate(@Param("trainNumber") String trainNumber, 
                                                @Param("ridingDate") Date ridingDate);
    
    /**
     * Update order status by order number
     *
     * @param orderSn the order number
     * @param status the new status
     * @return the number of rows affected
     */
    int updateStatusByOrderSn(@Param("orderSn") String orderSn, @Param("status") Integer status);
    
    /**
     * Count orders by status
     *
     * @param status the order status
     * @return the count
     */
    int countByStatus(Integer status);
}