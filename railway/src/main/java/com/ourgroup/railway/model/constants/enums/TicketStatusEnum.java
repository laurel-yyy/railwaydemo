package com.ourgroup.railway.model.constants.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Ticket status enumeration
 */
@RequiredArgsConstructor
public enum TicketStatusEnum {

    /**
     * Not paid yet
     */
    UNPAID(0),

    /**
     * Payment completed
     */
    PAID(1),

    /**
     * Passenger has boarded
     */
    BOARDED(2),

    /**
     * Ticket changed
     */
    CHANGED(3),

    /**
     * Ticket refunded
     */
    REFUNDED(4),

    /**
     * Order closed/cancelled
     */
    CLOSED(5);

    @Getter
    private final Integer code;
}