package com.ourgroup.railway.model.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Order item database entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDO {

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
     * user id
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
     * carriage number
     */
    private String carriageNumber;

    /**
     * seat type
     */
    private Integer seatType;

    /**
     * seat number
     */
    private String seatNumber;

    /**
     * real name
     */
    private String realName;

    /**
     * id type
     */
    private Integer idType;

    /**
     * id card
     */
    private String idCard;

    /**
     * phone
     */
    private String phone;

    /**
     * order status
     */
    private Integer status;

    /**
     * amount
     */
    private Integer amount;

    /**
     * ticket type
     */
    private Integer ticketType;
}