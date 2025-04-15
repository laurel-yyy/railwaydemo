package com.ourgroup.railway.model.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Ticket order detail response DTO
 */
@Data
public class TicketOrderDetailRespDTO {

    /**
     * id
     */
    private Long id;

    /**
     * order number
     */
    private String orderSn;

    /**
     * user id
     */
    private String userId;

    /**
     * username
     */
    private String username;
    
    /**
     * train id
     */
    private Long trainId;

    /**
     * departure station
     */
    private String departure;

    /**
     * arrival station
     */
    private String arrival;

    /**
     * riding date
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ridingDate;

    /**
     * order time
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date orderTime;

    /**
     * train number
     */
    private String trainNumber;

    /**
     * departure time
     */
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private Date departureTime;

    /**
     * arrival time
     */
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private Date arrivalTime;
    
    /**
     * order source
     */
    private Integer source;
    
    /**
     * order status
     */
    private Integer status;
    
    /**
     * payment type
     */
    private Integer payType;
    
    /**
     * payment time
     */
    private Date payTime;
    
    /**
     * create time
     */
    private Date createTime;
    
    /**
     * update time
     */
    private Date updateTime;
    
    /**
     * total amount
     */
    private Integer totalAmount;

    /**
     * passenger details
     */
    private List<TicketOrderPassengerDetailRespDTO> passengerDetails;
}