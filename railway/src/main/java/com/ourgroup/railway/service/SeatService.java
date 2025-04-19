package com.ourgroup.railway.service;

import java.util.List;

import com.ourgroup.railway.model.dto.domain.SeatTypeCountDTO;
import com.ourgroup.railway.model.dto.resp.TrainPurchaseTicketRespDTO;

public interface SeatService {
    
    List<String> listAvailableSeat(String trainId, String carriageNumber, Integer seatType, String departure, String arrival);

    List<Integer> listSeatRemainingTicket(String trainId, String departure, String arrival, List<String> trainCarriageList);

    List<String> listUsableCarriageNumber(String trainId, Integer carriageType, String departure, String arrival);

    List<SeatTypeCountDTO> listSeatTypeCount(Long trainId, String startStation, String endStation, List<Integer> seatTypes);

    void lockSeat(String trainId, String departure, String arrival, List<TrainPurchaseTicketRespDTO> trainPurchaseTicketRespList);

    void unlock(String trainId, String departure, String arrival, List<TrainPurchaseTicketRespDTO> trainPurchaseTicketResults);
}
