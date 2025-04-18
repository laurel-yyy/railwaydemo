package com.ourgroup.railway.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ourgroup.railway.framework.cache.DistributedCache;
import com.ourgroup.railway.framework.constant.RailwayConstant;
import com.ourgroup.railway.framework.constant.RedisKeyConstant;
import com.ourgroup.railway.framework.enums.SeatStatusEnum;
import com.ourgroup.railway.mapper.SeatMapper;
import com.ourgroup.railway.model.dao.SeatDO;
import com.ourgroup.railway.model.dto.domain.RouteDTO;
import com.ourgroup.railway.model.dto.domain.SeatTypeCountDTO;
import com.ourgroup.railway.model.dto.resp.TrainPurchaseTicketRespDTO;
import com.ourgroup.railway.service.SeatService;
import com.ourgroup.railway.service.TrainStationService;

import groovy.util.logging.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatServiceImpl implements SeatService {

    private final SeatMapper seatMapper;
    private final TrainStationService trainStationService;
    private final DistributedCache distributedCache;
    private final RedissonClient redissonClient;

    @Override
    public List<String> listAvailableSeat(String trainId, String carriageNumber, Integer seatType, String departure, String arrival) {

        List<SeatDO> seatDOList = seatMapper.selectAvailableSeatList(Long.parseLong(trainId), carriageNumber, seatType, departure, arrival, SeatStatusEnum.AVAILABLE.getCode());
        return seatDOList.stream().map(SeatDO::getSeatNumber).collect(Collectors.toList());
    }

    @Override
    public List<Integer> listSeatRemainingTicket(String trainId, String departure, String arrival, List<String> trainCarriageList) {
        // String keySuffix = String.join("_", trainId, departure, arrival);
        // String redisKey = RedisKeyConstant.TRAIN_STATION_CARRIAGE_REMAINING_TICKET + keySuffix;
        
        // // 先尝试从缓存获取
        // if (distributedCache.hasKey(redisKey)) {
        //     StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        //     List<Object> trainStationCarriageRemainingTicket =
        //             stringRedisTemplate.opsForHash().multiGet(redisKey, Arrays.asList(trainCarriageList.toArray()));
        //     if (!CollectionUtils.isEmpty(trainStationCarriageRemainingTicket)) {
        //         return trainStationCarriageRemainingTicket.stream()
        //                 .filter(each -> each != null)  // 添加null过滤
        //                 .map(each -> Integer.parseInt(each.toString()))
        //                 .collect(Collectors.toList());
        //     }
        // }
        
        // // 缓存中没有数据，需要从数据库查询
        // RLock lock = redissonClient.getLock(RedisKeyConstant.LOCK_TRAIN_STATION_CARRIAGE_REMAINING_TICKET + keySuffix);
        // lock.lock();
        
        // try {
        //     // 双重检查，避免重复查询数据库
        //     if (distributedCache.hasKey(redisKey)) {
        //         StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        //         List<Object> trainStationCarriageRemainingTicket =
        //                 stringRedisTemplate.opsForHash().multiGet(redisKey, Arrays.asList(trainCarriageList.toArray()));
        //         if (!CollectionUtils.isEmpty(trainStationCarriageRemainingTicket)) {
        //             return trainStationCarriageRemainingTicket.stream()
        //                     .map(each -> Integer.parseInt(each.toString()))
        //                     .collect(Collectors.toList());
        //         }
        //     }
            
        //     SeatDO seatDO = SeatDO.builder()
        //             .trainId(Long.parseLong(trainId))
        //             .startStation(departure)
        //             .endStation(arrival)
        //             .build();
        //     List<Integer> remainingTickets = seatMapper.listSeatRemainingTicket(seatDO, trainCarriageList);
            
        //     if (!CollectionUtils.isEmpty(remainingTickets)) {
        //         StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        //         Map<String, String> carriageTicketMap = new HashMap<>();
                
        //         for (int i = 0; i < trainCarriageList.size(); i++) {
        //             if (i < remainingTickets.size()) {
        //                 carriageTicketMap.put(trainCarriageList.get(i), String.valueOf(remainingTickets.get(i)));
        //             }
        //         }
                
        //         if (!carriageTicketMap.isEmpty()) {
        //             stringRedisTemplate.opsForHash().putAll(redisKey, carriageTicketMap);
        //             stringRedisTemplate.expire(redisKey, RailwayConstant.ADVANCE_TICKET_DAY, TimeUnit.DAYS);
        //         }
        //     }
            
        //     return remainingTickets;
        // } catch (Exception e) {
            SeatDO seatDO = SeatDO.builder()
                    .trainId(Long.parseLong(trainId))
                    .startStation(departure)
                    .endStation(arrival)
                    .build();
            return seatMapper.listSeatRemainingTicket(seatDO, trainCarriageList);
        // } finally {
        //     // 释放锁
        //     if (lock.isHeldByCurrentThread()) {
        //         lock.unlock();
        //     }
        // }
    }

    @Override
    public void lockSeat(String trainId, String departure, String arrival, List<TrainPurchaseTicketRespDTO> trainPurchaseTicketRespList) {
        List<RouteDTO> routeList = trainStationService.listTakeoutTrainStationRoute(trainId, departure, arrival);
        
        List<SeatDO> batchUpdateList = new ArrayList<>();
        
        for (TrainPurchaseTicketRespDTO ticket : trainPurchaseTicketRespList) {
            for (RouteDTO route : routeList) {
                SeatDO seatDO = SeatDO.builder()
                            .trainId(Long.parseLong(trainId))
                            .carriageNumber(ticket.getCarriageNumber())
                            .startStation(route.getStartStation())
                            .endStation(route.getEndStation())
                            .seatNumber(ticket.getSeatNumber())
                            .seatStatus(SeatStatusEnum.LOCKED.getCode())
                            .build();
                
                batchUpdateList.add(seatDO);
            }
        }
        
        if (!batchUpdateList.isEmpty()) {
            seatMapper.batchUpdateSeatStatus(batchUpdateList);
        }
    }

    @Override
    public void unlock(String trainId, String departure, String arrival, List<TrainPurchaseTicketRespDTO> trainPurchaseTicketResults) {
        List<RouteDTO> routeList = trainStationService.listTakeoutTrainStationRoute(trainId, departure, arrival);
        
        // 构建批量更新的参数列表
        List<SeatDO> batchUpdateList = new ArrayList<>();
        
        for (TrainPurchaseTicketRespDTO ticket : trainPurchaseTicketResults) {
            for (RouteDTO route : routeList) {
                SeatDO seatDO = SeatDO.builder()
                            .trainId(Long.parseLong(trainId))
                            .carriageNumber(ticket.getCarriageNumber())
                            .startStation(route.getStartStation())
                            .endStation(route.getEndStation())
                            .seatNumber(ticket.getSeatNumber())
                            .seatStatus(SeatStatusEnum.LOCKED.getCode())
                            .build();
                
                batchUpdateList.add(seatDO);
            }
        }
        
        // 执行批量更新
        if (!batchUpdateList.isEmpty()) {
            seatMapper.batchUpdateSeatStatus(batchUpdateList);
        }
    }

    @Override
    public List<String> listUsableCarriageNumber(String trainId, Integer carriageType, String departure, String arrival) {
        return seatMapper.listUsableCarriageNumber(trainId, carriageType, departure, arrival, SeatStatusEnum.AVAILABLE.getCode());
    }

    @Override
    public List<SeatTypeCountDTO> listSeatTypeCount(Long trainId, String startStation, String endStation,
            List<Integer> seatTypes) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listSeatTypeCount'");
    }
   
}
