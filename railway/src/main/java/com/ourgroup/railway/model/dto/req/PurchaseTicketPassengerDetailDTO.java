package com.ourgroup.railway.model.dto.req;

import lombok.Data;

/**
 * Passenger details entity for ticket purchase
 */
@Data
public class PurchaseTicketPassengerDetailDTO {
    /**
     * Passenger ID
     */
    private Long id;
    
    /**
     * Passenger name
     */
    private String name;
    
    /**
     * ID type (1 = ID Card, 2 = Passport, etc.)
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
     * Passenger type (1 = Adult, 2 = Child, 3 = Student, etc.)
     */
    private Integer ticketType;
    
    /**
     * Seat type (1 = First Class, 2 = Second Class, etc.)
     */
    private Integer seatType;
    
    /**
     * Discount rate
     */
    private Integer discountRate;
}