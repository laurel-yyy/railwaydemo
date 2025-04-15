package com.ourgroup.railway.model.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Self ticket order detail response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketOrderDetailSelfRespDTO {

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
     * ticket type 0: adult 1: child 2: student 3: disabled military
     */
    private Integer ticketType;

    /**
     * amount
     */
    private Integer amount;
}