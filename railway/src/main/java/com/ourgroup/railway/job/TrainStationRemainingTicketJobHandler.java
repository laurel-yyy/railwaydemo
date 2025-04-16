package com.ourgroup.railway.job;

import com.ourgroup.railway.framework.cache.DistributedCache;
import com.ourgroup.railway.framework.constant.RedisKeyConstant;
import com.ourgroup.railway.mapper.TrainMapper;
import com.ourgroup.railway.mapper.TrainStationRelationMapper;
import com.ourgroup.railway.model.dao.TrainDO;
import com.ourgroup.railway.model.dao.TrainStationRelationDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Job for initializing remaining ticket data in Redis.
 * Only for development/testing; do not use in production.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TrainStationRemainingTicketJobHandler {

    private final TrainMapper trainMapper;
    private final TrainStationRelationMapper trainStationRelationMapper;
    private final DistributedCache distributedCache;

    /**
     * Trigger manually via GET request.
     * Example: http://localhost:8080/api/job/cache-init
     */
    @GetMapping("/api/job/cache-init")
    public void initRemainingTicketsToRedis() {
        log.info("Start initializing remaining train tickets into Redis...");

        List<TrainDO> trainList = trainMapper.selectAllTrains(); // Use your own MyBatis method

        for (TrainDO train : trainList) {
            List<TrainStationRelationDO> relationList = trainStationRelationMapper.selectByTrainId(train.getId());

            if (relationList == null || relationList.isEmpty()) continue;

            for (TrainStationRelationDO relation : relationList) {
                String departure = relation.getDeparture();
                String arrival = relation.getArrival();

                // Build default remaining tickets based on train type
                Map<String, String> remainingTickets = new HashMap<>();
                switch (train.getTrainType()) {
                    case 0 -> {
                        remainingTickets.put("0", "10");
                        remainingTickets.put("1", "140");
                        remainingTickets.put("2", "810");
                    }
                    case 1 -> {
                        remainingTickets.put("3", "96");
                        remainingTickets.put("4", "192");
                        remainingTickets.put("5", "216");
                        remainingTickets.put("13", "216");
                    }
                    case 2 -> {
                        remainingTickets.put("6", "96");
                        remainingTickets.put("7", "192");
                        remainingTickets.put("8", "216");
                        remainingTickets.put("13", "216");
                    }
                }

                String key = RedisKeyConstant.TRAIN_STATION_REMAINING_TICKET +
                        train.getId() + "_" + departure + "_" + arrival;

                StringRedisTemplate redis = (StringRedisTemplate) distributedCache.getInstance();
                redis.opsForHash().putAll(key, remainingTickets);
                redis.expire(key, 15, TimeUnit.DAYS);

                log.info("Initialized Redis for trainId={}, from={} to={}, key={}", train.getId(), departure, arrival, key);
            }
        }

        log.info("Finished Redis ticket initialization.");
    }
}
