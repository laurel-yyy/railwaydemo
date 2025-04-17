package com.ourgroup.railway.model.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ticket order passenger detail response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketOrderPassengerDetailRespDTO {

    /**
     * id
     */
    private Long id;

    /**
     * user id
     */
    private String userId;

    /**
     * username
     */
    private String username;

    /**
     * seat type
     */
    private Integer seatType;

    /**
     * carriage number
     */
    private String carriageNumber;

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
     * ticket type 0: adult 1: child 2: student 3: disabled military
     */
    private Integer ticketType;

    /**
     * amount
     */
    private Integer amount;

    /**
     * status
     */
    private Integer status;

    /**
     * status name
     */
    private String statusName;
}