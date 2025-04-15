package com.ourgroup.railway.model.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Passenger order relationship entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemPassengerDO {

    /**
     * id
     */
    private Long id;
    
    /**
     * create time
     */
    private Date createTime;
    
    /**
     * update time
     */
    private Date updateTime;
    
    /**
     * order number
     */
    private String orderSn;

    /**
     * id type
     */
    private Integer idType;

    /**
     * id card
     */
    private String idCard;
}