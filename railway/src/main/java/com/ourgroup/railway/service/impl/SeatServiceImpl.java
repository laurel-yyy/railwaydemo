package com.ourgroup.railway.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ourgroup.railway.framework.cache.DistributedCache;
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

    @Override
    public List<String> listAvailableSeat(String trainId, String carriageNumber, Integer seatType, String departure, String arrival) {

        List<SeatDO> seatDOList = seatMapper.selectAvailableSeatList(Long.parseLong(trainId), carriageNumber, seatType, departure, arrival, SeatStatusEnum.AVAILABLE.getCode());
        return seatDOList.stream().map(SeatDO::getSeatNumber).collect(Collectors.toList());
    }

    @Override
    public List<Integer> listSeatRemainingTicket(String trainId, String departure, String arrival, List<String> trainCarriageList) {
        String keySuffix = String.join("_", trainId, departure, arrival);
        if (distributedCache.hasKey(RedisKeyConstant.TRAIN_STATION_CARRIAGE_REMAINING_TICKET + keySuffix)) {
            StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
            List<Object> trainStationCarriageRemainingTicket =
                    stringRedisTemplate.opsForHash().multiGet(RedisKeyConstant.TRAIN_STATION_CARRIAGE_REMAINING_TICKET + keySuffix, Arrays.asList(trainCarriageList.toArray()));
            if (!CollectionUtils.isEmpty(trainStationCarriageRemainingTicket)) {
                return trainStationCarriageRemainingTicket.stream().map(each -> Integer.parseInt(each.toString())).collect(Collectors.toList());
            }
        }
        SeatDO seatDO = SeatDO.builder()
                .trainId(Long.parseLong(trainId))
                .startStation(departure)
                .endStation(arrival)
                .build();
        return seatMapper.listSeatRemainingTicket(seatDO, trainCarriageList);
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
    public List<String> listUsableCarriageNumber(String trainId, Integer carriageType, String departure,
            String arrival) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listUsableCarriageNumber'");
    }

    @Override
    public List<SeatTypeCountDTO> listSeatTypeCount(Long trainId, String startStation, String endStation,
            List<Integer> seatTypes) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listSeatTypeCount'");
    }
   
}
