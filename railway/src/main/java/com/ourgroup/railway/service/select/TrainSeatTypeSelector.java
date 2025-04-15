package com.ourgroup.railway.service.select;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourgroup.railway.framework.cache.DistributedCache;
import com.ourgroup.railway.framework.constant.RailwayConstant;
import com.ourgroup.railway.framework.constant.RedisKeyConstant;
import com.ourgroup.railway.mapper.SeatMapper;
import com.ourgroup.railway.mapper.TrainStationPriceMapper;
import com.ourgroup.railway.model.dao.SeatDO;
import com.ourgroup.railway.model.dao.TrainStationPriceDO;
import com.ourgroup.railway.model.dto.req.PurchaseTicketReqDTO;
import com.ourgroup.railway.model.dto.resp.TrainPurchaseTicketRespDTO;
import com.ourgroup.railway.service.PassengerService;
import com.ourgroup.railway.service.SeatService;
// import com.ourgroup.railway.model.dto.PassengerDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Train seat type selector implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TrainSeatTypeSelector {

    private final SeatMapper seatMapper;
    private final SeatService seatService;
    private final TrainStationPriceMapper trainStationPriceMapper;
    private final DistributedCache distributedCache;
    private final PassengerService passengerService; // Passenger service dependency

    /**
     * Select and allocate seats for ticket purchase
     * 
     * @param requestParam Purchase ticket request parameters
     * @return List of allocated seats with details
     */
    @Transactional(rollbackFor = Exception.class)
    public List<TrainPurchaseTicketRespDTO> select(PurchaseTicketReqDTO requestParam) {
        List<TrainPurchaseTicketRespDTO> result = new ArrayList<>();
        String trainId = requestParam.getTrainId();
        String departure = requestParam.getDeparture();
        String arrival = requestParam.getArrival();
        
        // Get passenger ID from request
        String passengerId = requestParam.getPassengerId();
        if (passengerId == null) {
            log.error("Passenger ID cannot be null");
            return result;
        }
        
        // Get passenger details by ID
        // PassengerDTO passenger = passengerService.getPassengerById(passengerId);
        // if (passenger == null) {
        //     log.error("Passenger not found with ID: {}", passengerId);
        //     return result;
        // }
        
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        
        // Get requested seat type
        Integer seatType = requestParam.getSeatType();
        
        // Find ticket price for this route and seat type
        TrainStationPriceDO price = getTrainStationPrice(Long.parseLong(trainId), departure, arrival, seatType);
        if (price == null) {
            log.error("Price not found for train: {}, departure: {}, arrival: {}, seatType: {}", 
                      trainId, departure, arrival, seatType);
            throw new RuntimeException("Seat price information not found");
        }
        
        // Check seat availability
        String keySuffix = String.join("_", trainId, departure, arrival);
        String remainingTicketsKey = RedisKeyConstant.TRAIN_STATION_REMAINING_TICKET + keySuffix;
        
        // Get remaining tickets count from Redis
        Object remainingObj = stringRedisTemplate.opsForHash().get(remainingTicketsKey, String.valueOf(seatType));
        int remainingCount = remainingObj != null ? Integer.parseInt(remainingObj.toString()) : 0;
        
        if (remainingCount <= 0) {
            log.error("No available seats for train: {}, departure: {}, arrival: {}, seatType: {}", 
                      trainId, departure, arrival, seatType);
            throw new RuntimeException("No available seats for selected type");
        }
        
        // Find an available seat
        SeatDO availableSeat = findAvailableSeat(Long.parseLong(trainId), seatType, departure, arrival);
        if (availableSeat == null) {
            log.error("Failed to find available seat for train: {}, departure: {}, arrival: {}, seatType: {}", 
                      trainId, departure, arrival, seatType);
            throw new RuntimeException("Failed to allocate seat");
        }
        
        // Create seats to lock
        List<TrainPurchaseTicketRespDTO> lockSeats = new ArrayList<>();
        TrainPurchaseTicketRespDTO seatToLock = TrainPurchaseTicketRespDTO.builder()
                .carriageNumber(availableSeat.getCarriageNumber())
                .seatNumber(availableSeat.getSeatNumber())
                .passengerId(passengerId)
                .build();
        lockSeats.add(seatToLock);
        
        // Lock the seat
        seatService.lockSeat(trainId, departure, arrival, lockSeats);
        
        // if (!seatLocked) {
        //     log.error("Failed to lock seat ID: {}", availableSeat.getId());
        //     throw new RuntimeException("Failed to secure seat");
        // }
        
        // Update remaining tickets count in Redis
        stringRedisTemplate.opsForHash().increment(remainingTicketsKey, String.valueOf(seatType), -1);
        
        // Create response with selected seat
        TrainPurchaseTicketRespDTO ticketResp = TrainPurchaseTicketRespDTO.builder()
                .carriageNumber(availableSeat.getCarriageNumber())
                .seatNumber(availableSeat.getSeatNumber())
                .passengerId(passengerId)
                // .realName(passenger.getRealName())
                .seatType(seatType)
                .amount(price.getPrice())
                .build();
        
        result.add(ticketResp);
        
        return result;
    }

    /**
     * Find an available seat for the given train, seat type, and route
     * 
     * @param trainId Train ID
     * @param seatType Seat type
     * @param departure Departure station
     * @param arrival Arrival station
     * @return Available seat or null if none found
     */
    private SeatDO findAvailableSeat(Long trainId, Integer seatType, String departure, String arrival) {
        final Integer SEAT_STATUS_AVAILABLE = 0;
    
        // find available seats
        List<SeatDO> seats = seatMapper.selectAvailableSeatList(
                trainId, 
                null, 
                seatType, 
                departure, 
                arrival, 
                SEAT_STATUS_AVAILABLE);
        
        // get the first available seat
        if (seats != null && !seats.isEmpty()) {
            return seats.get(0);
        } else {
            return null;
        }
    }
    
    /**
     * Get the price information for a specific train, route, and seat type
     * 
     * @param trainId Train ID
     * @param departure Departure station
     * @param arrival Arrival station
     * @param seatType Seat type
     * @return Price information or null if not found
     */
    private TrainStationPriceDO getTrainStationPrice(Long trainId, String departure, String arrival, Integer seatType) {
        // Build cache key using existing constant, adding seat type
        String cacheKey = String.format(RedisKeyConstant.TRAIN_STATION_PRICE, 
                trainId, departure, arrival) + ":" + seatType;
        
        return distributedCache.safeGet(
                cacheKey,
                TrainStationPriceDO.class,
                () -> {
                    // Query price list for all seat types
                    List<TrainStationPriceDO> priceList = trainStationPriceMapper.selectByTrainAndStations(trainId, departure, arrival);
                    if (priceList != null && !priceList.isEmpty()) {
                        // Filter to find price for the specified seat type
                        for (TrainStationPriceDO price : priceList) {
                            if (price.getSeatType().equals(seatType)) {
                                return price; // Return matching price
                            }
                        }
                    }
                    return null; // Return null if no matching price found
                },
                RailwayConstant.ADVANCE_TICKET_DAY,
                TimeUnit.DAYS);
    }
}