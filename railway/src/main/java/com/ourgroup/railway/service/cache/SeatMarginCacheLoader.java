package com.ourgroup.railway.service.cache;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ourgroup.railway.framework.cache.DistributedCache;
import com.ourgroup.railway.framework.cache.toolkit.CacheUtil;
import com.ourgroup.railway.framework.constant.RailwayConstant;
import com.ourgroup.railway.framework.enums.SeatStatusEnum;
import com.ourgroup.railway.mapper.SeatMapper;
import com.ourgroup.railway.mapper.TrainMapper;
import com.ourgroup.railway.model.dao.SeatDO;
import com.ourgroup.railway.model.dao.TrainDO;
import com.ourgroup.railway.model.dto.domain.RouteDTO;
import com.ourgroup.railway.service.TrainStationService;
import com.ourgroup.railway.framework.constant.RedisKeyConstant;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SeatMarginCacheLoader {

    private final TrainMapper trainMapper;
    private final SeatMapper seatMapper;
    private final DistributedCache distributedCache;
    private final RedissonClient redissonClient;
    private final TrainStationService trainStationService;

    public Map<String, String> load(String trainId, String seatType, String departure, String arrival) {
        Map<String, Map<String, String>> trainStationRemainingTicketMaps = new LinkedHashMap<>();
        String keySuffix = CacheUtil.buildKey(trainId, departure, arrival);

        RLock lock = redissonClient.getLock(String.format(RedisKeyConstant.LOCK_SAFE_LOAD_SEAT_MARGIN_GET, keySuffix));
        lock.lock();
        try {
            StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
            Object quantityObj = stringRedisTemplate.opsForHash().get(RedisKeyConstant.TRAIN_STATION_REMAINING_TICKET + keySuffix, seatType);
            if (CacheUtil.isNullOrBlank(quantityObj)) {
                TrainDO trainDO = distributedCache.safeGet(
                    RedisKeyConstant.TRAIN_INFO + trainId,
                        TrainDO.class,
                        () -> trainMapper.selectById(Long.parseLong(trainId)),
                        RailwayConstant.ADVANCE_TICKET_DAY,
                        TimeUnit.DAYS
                );
                List<RouteDTO> routeDTOList = trainStationService.listTrainStationRoute(trainId, trainDO.getStartStation(), trainDO.getEndStation());
                if (!CollectionUtils.isEmpty(routeDTOList)) {

                    for (RouteDTO each : routeDTOList) {
                        Map<String, String> trainStationRemainingTicket = new LinkedHashMap<>();
                        trainStationRemainingTicket.put("0", selectSeatMargin(trainId, 0, each.getStartStation(), each.getEndStation()));
                        trainStationRemainingTicket.put("1", selectSeatMargin(trainId, 1, each.getStartStation(), each.getEndStation()));
                        trainStationRemainingTicket.put("2", selectSeatMargin(trainId, 2, each.getStartStation(), each.getEndStation()));
                        trainStationRemainingTicket.put("3", selectSeatMargin(trainId, 3, each.getStartStation(), each.getEndStation()));
                        trainStationRemainingTicket.put("4", selectSeatMargin(trainId, 4, each.getStartStation(), each.getEndStation()));
                        trainStationRemainingTicket.put("5", selectSeatMargin(trainId, 5, each.getStartStation(), each.getEndStation()));
                        String actualKeySuffix = CacheUtil.buildKey(trainId, each.getStartStation(), each.getEndStation());
                        trainStationRemainingTicketMaps.put(RedisKeyConstant.TRAIN_STATION_REMAINING_TICKET + actualKeySuffix, trainStationRemainingTicket);
                    }
                } else {
                    Map<String, String> trainStationRemainingTicket = new LinkedHashMap<>();
                    List<Integer> seatTypes = Arrays.asList(0, 1, 2, 3, 4, 5);
                    seatTypes.forEach(stype -> 
                        trainStationRemainingTicket.put(String.valueOf(stype), "0")
                    );
                    trainStationRemainingTicketMaps.put(RedisKeyConstant.TRAIN_STATION_REMAINING_TICKET + keySuffix, trainStationRemainingTicket);
                }

                trainStationRemainingTicketMaps.forEach((cacheKey, cacheMap) -> stringRedisTemplate.opsForHash().putAll(cacheKey, cacheMap));
            }
        } finally {
            lock.unlock();
        }
        return Optional.ofNullable(trainStationRemainingTicketMaps.get(RedisKeyConstant.TRAIN_STATION_REMAINING_TICKET + keySuffix))
                .orElse(new LinkedHashMap<>());
    }

    private String selectSeatMargin(String trainId, Integer type, String departure, String arrival) {

        Integer count = seatMapper.countAvailableSeats(
                Long.parseLong(trainId), 
                type, 
                SeatStatusEnum.AVAILABLE.getCode(), 
                departure, 
                arrival);
        
        return Optional.ofNullable(count)
                .map(String::valueOf)
                .orElse("0");
    }
}
