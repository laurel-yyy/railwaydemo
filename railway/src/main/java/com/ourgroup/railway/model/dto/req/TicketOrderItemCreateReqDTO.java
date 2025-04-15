package com.ourgroup.railway.model.dto.req;

import lombok.Data;

/**
 * Ticket order item creation request DTO
 */
@Data
public class TicketOrderItemCreateReqDTO {

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
     * passenger id
     */
    private String passengerId;

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
     * amount
     */
    private Integer amount;

    /**
     * ticket type
     */
    private Integer ticketType;
}