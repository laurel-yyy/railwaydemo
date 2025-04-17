package com.ourgroup.railway.model.dto.req;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Ticket order creation request DTO
 */
@Data
public class TicketOrderCreateReqDTO {
    
    /**
     * Train ID
     */
    private Long trainId;
    
    /**
     * Train number
     */
    private String trainNumber;
    
    /**
     * Departure station
     */
    private String departure;
    
    /**
     * Arrival station
     */
    private String arrival;
    
    /**
     * Riding date
     */
    private Date ridingDate;
    
    /**
     * Departure time
     */
    private Date departureTime;
    
    /**
     * Arrival time
     */
    private Date arrivalTime;
    
    /**
     * User ID
     */
    private String userId;
    
    /**
     * Username
     */
    private String username;
    
    /**
     * Total amount
     */
    private Integer amount;
    
    /**
     * Passenger information
     */
    private List<PassengerInfo> passengers;
    
    private Integer source;

    private List<TicketOrderItemCreateReqDTO> ticketOrderItems;
    
    /**
     * Passenger information
     */
    @Data
    public static class PassengerInfo {
        
        /**
         * Passenger real name
         */
        private String realName;
        
        /**
         * ID type
         * 1: ID Card
         * 2: Passport
         * 3: Other
         */
        private Integer idType;
        
        /**
         * ID card number
         */
        private String idCard;
        
        /**
         * Phone number
         */
        private String phone;
        
        /**
         * Seat type
         * 1: First class
         * 2: Second class
         * 3: Business class
         */
        private Integer seatType;
        
        /**
         * Carriage number
         */
        private String carriageNumber;
        
        /**
         * Seat number
         */
        private String seatNumber;
        
        /**
         * Ticket amount
         */
        private Integer amount;
        
        /**
         * Ticket type
         * 1: Adult
         * 2: Child
         * 3: Student
         * 4: Elderly
         */
        private Integer ticketType;


    }
}