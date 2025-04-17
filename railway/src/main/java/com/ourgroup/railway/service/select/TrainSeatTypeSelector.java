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
import com.ourgroup.railway.service.SeatService;
import com.ourgroup.railway.service.handler.TrainPurchaseTicketHandler;

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

    private final TrainPurchaseTicketHandler trainPurchaseTicketHandler;

    /**
     * Select and allocate seats for ticket purchase
     * 
     * @param requestParam Purchase ticket request parameters
     * @return List of allocated seats with details
     * @throws Exception 
     */
    @Transactional(rollbackFor = Exception.class)
    public List<TrainPurchaseTicketRespDTO> select(PurchaseTicketReqDTO requestParam) throws Exception {
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
        // Object remainingObj = stringRedisTemplate.opsForHash().get(remainingTicketsKey, String.valueOf(seatType));
        int remainingCount = distributedCache.safeGet(
            remainingTicketsKey + ":" + seatType, 
            Integer.class, 
            () -> {
                // 使用 SeatMapper 查询可用座位数量
                Integer availableSeats = seatMapper.countAvailableSeats(
                    Long.parseLong(trainId),     // 火车ID
                    seatType,                    // 座位类型
                    0,                           // 座位状态（可用）
                    departure,                   // 出发站
                    arrival                      // 到达站
                );
                
                return availableSeats != null ? availableSeats : 0;
            }, 
            RailwayConstant.ADVANCE_TICKET_DAY
        );
        
        if (remainingCount <= 0) {
            log.error("No available seats for train: {}, departure: {}, arrival: {}, seatType: {}",
                       trainId, departure, arrival, seatType);
            throw new RuntimeException("No available seats for selected type");
        }
        
        // Find an available seat

        TrainPurchaseTicketRespDTO seatToLock = trainPurchaseTicketHandler.executeResp(requestParam);
        List<TrainPurchaseTicketRespDTO> lockSeats = new ArrayList<>();
        lockSeats.add(seatToLock);
        
        // Lock the seat
        seatService.lockSeat(trainId, departure, arrival, lockSeats);
        
        // Update remaining tickets count in Redis
        stringRedisTemplate.opsForHash().increment(remainingTicketsKey, String.valueOf(seatType), -1);
        
        result.add(seatToLock);
        
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
        // 日志记录输入参数
        log.info("获取车票价格 - trainId: {}, departure: {}, arrival: {}, seatType: {}", 
        trainId, departure, arrival, seatType);
        
        // Build cache key using existing constant, adding seat type
        String cacheKey = String.format(RedisKeyConstant.TRAIN_STATION_PRICE, 
                trainId, departure, arrival) + ":" + seatType;
        log.info("Cache key for train station price: {}", cacheKey);
        
        return distributedCache.safeGet(
                cacheKey,
                TrainStationPriceDO.class,
                () -> {
                    // Query price list for all seat types
                    List<TrainStationPriceDO> priceList = trainStationPriceMapper.selectByTrainAndStations(trainId, departure, arrival);
                    log.info("数据库查询结果 - priceList size: {}", priceList != null ? priceList.size() : 0);
                    if (priceList != null && !priceList.isEmpty()) {
                        // 详细记录每条价格记录
                        for (TrainStationPriceDO price : priceList) {
                            log.info("价格记录详情 - trainId: {}, departure: {}, arrival: {}, seatType: {}, price: {}", 
                                    price.getTrainId(), price.getDeparture(), price.getArrival(), 
                                    price.getSeatType(), price.getPrice());
                        }
                        
                        // Filter to find price for the specified seat type
                        for (TrainStationPriceDO price : priceList) {
                            if (price.getSeatType().equals(seatType)) {
                                log.info("找到匹配的价格记录: {}", price);
                                return price; // Return matching price
                            }
                        }

                        log.warn("未找到匹配的座位类型: {}", seatType);
                    } else {
                        log.warn("未查询到任何价格记录");
                    }
        
                    return null;
                },
                RailwayConstant.ADVANCE_TICKET_DAY,
                TimeUnit.DAYS);
    }
}