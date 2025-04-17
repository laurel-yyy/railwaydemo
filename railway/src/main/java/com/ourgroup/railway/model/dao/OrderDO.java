package com.ourgroup.railway.model.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Order database entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDO {
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
     * user id same with UserDO
     */
    private Long userId;

    /**
     * username
     */
    private String username;

    /**
     * train id
     */
    private Long trainId;

    /**
     * train number
     */
    private String trainNumber;

    /**
     * departure station
     */
    private String departure;

    /**
     * arrival station
     */
    private String arrival;

    /**
     * order source
     */
    private Integer source;

    /**
     * order status
     */
    private Integer status;

    /**
     * order time
     */
    private Date orderTime;

    /**
     * payment type
     */
    private Integer payType;

    /**
     * payment time
     */
    private Date payTime;

    /**
     * riding date
     */
    private Date ridingDate;

    /**
     * departure time
     */
    private Date departureTime;

    /**
     * arrival time
     */
    private Date arrivalTime;

    // No need for explicit getters and setters due to @Data annotation from Lombok
}