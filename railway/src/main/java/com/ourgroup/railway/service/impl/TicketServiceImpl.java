package com.ourgroup.railway.service.impl;

import org.springframework.util.CollectionUtils;
import org.apache.shardingsphere.distsql.parser.autogen.KernelDistSQLStatementParser.UserContext;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.s;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourgroup.railway.framework.cache.DistributedCache;
import com.ourgroup.railway.model.dto.req.PurchaseTicketReqDTO;
import com.ourgroup.railway.model.dto.req.TicketOrderCreateReqDTO;
import com.ourgroup.railway.model.dto.req.TicketPageQueryReqDTO;
import com.ourgroup.railway.model.dto.resp.TicketOrderDetailRespDTO;
import com.ourgroup.railway.model.dto.resp.TicketPageQueryRespDTO;
import com.ourgroup.railway.model.dto.resp.TicketPurchaseRespDTO;
import com.ourgroup.railway.model.dto.resp.TrainPurchaseTicketRespDTO;
import com.ourgroup.railway.service.select.TrainSeatTypeSelector;
import com.ourgroup.railway.model.constants.enums.TicketStatusEnum;
import com.ourgroup.railway.service.OrderService;
import com.ourgroup.railway.service.TicketService;
import com.ourgroup.railway.service.cache.SeatMarginCacheLoader;
import com.ourgroup.railway.model.dao.TicketDO;
import com.ourgroup.railway.model.dao.TrainDO;
import com.ourgroup.railway.model.dao.TrainStationPriceDO;
import com.ourgroup.railway.model.dao.TrainStationRelationDO;
import com.ourgroup.railway.model.dao.UserDO;
import com.ourgroup.railway.model.dto.domain.SeatClassDTO;
import com.ourgroup.railway.model.dto.domain.TicketListDTO;
import com.ourgroup.railway.framework.constant.RedisKeyConstant;
import com.ourgroup.railway.framework.result.Result;
import com.ourgroup.railway.framework.toolkit.JWTUtil;
import com.ourgroup.railway.mapper.TrainMapper;
import com.ourgroup.railway.mapper.TrainStationPriceMapper;
import com.ourgroup.railway.mapper.TrainStationRelationMapper;
import com.ourgroup.railway.mapper.UserMapper;
import com.ourgroup.railway.framework.constant.RailwayConstant;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.HttpServletRequest;


@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService{

    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TrainStationRelationMapper trainStationRelationMapper;
    private final TrainMapper trainMapper;
    private final TrainStationPriceMapper trainStationPriceMapper;

    private final DistributedCache distributedCache;
    private final RedissonClient redissonClient;
    private final SeatMarginCacheLoader seatMarginCacheLoader;

    private final OrderService orderService;


    private final ConfigurableEnvironment environment;
    private final UserMapper userMapper;

    private final HttpServletRequest request;

    @Value("${framework.cache.redis.prefix}")
    private String cahceRedisPrefix;
    
    private final TrainSeatTypeSelector trainSeatTypeSelector;

    @Override
    public TicketPageQueryRespDTO pageListQueryTicket(@RequestBody TicketPageQueryReqDTO requestParam) {
        // 添加日志输出，查看请求参数的值
        log.info("接收到查询请求参数: fromStation={}, toStation={}, departure={}, arrival={}",
        requestParam.getFromStation(), requestParam.getToStation(),
        requestParam.getDeparture(), requestParam.getArrival());
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        
        List<TicketListDTO> ticketList = new ArrayList<>();
        String trainStationCacheKey  = String.format(RedisKeyConstant.TRAIN_STATIONS, requestParam.getFromStation(), requestParam.getToStation());
        Map<Object, Object> trainStationMap = stringRedisTemplate.opsForHash().entries(trainStationCacheKey);

        if(CollectionUtils.isEmpty(trainStationMap)) {
            RLock lock = redissonClient.getLock(RedisKeyConstant.LOCK_TRAIN_STATIONS);
            lock.lock();

            try {
                trainStationMap = stringRedisTemplate.opsForHash().entries(trainStationCacheKey);
                if(CollectionUtils.isEmpty(trainStationMap)) {
                    List<TrainStationRelationDO> trainStationRelationList = trainStationRelationMapper.selectByDepartureAndArrival(requestParam.getFromStation(), requestParam.getToStation());

                    for(TrainStationRelationDO relation : trainStationRelationList) {
                        TrainDO trainDO = distributedCache.safeGet(
                                            RedisKeyConstant.TRAIN_INFO + relation.getTrainId(), 
                                            TrainDO.class,
                                            () -> trainMapper.selectById(relation.getTrainId()),
                                            RailwayConstant.ADVANCE_TICKET_DAY,
                                            TimeUnit.DAYS);

                        TicketListDTO result = new TicketListDTO();
                        
                        result.setTrainId(String.valueOf(trainDO.getId()));
                        result.setTrainNumber(trainDO.getTrainNumber());
                        result.setDepartureTime(formatTime(relation.getDepartureTime(), "HH:mm"));
                        result.setArrivalTime(formatTime(relation.getArrivalTime(), "HH:mm"));
                        result.setDuration(calculateHourDifference(relation.getDepartureTime(), relation.getArrivalTime()));
                        result.setDeparture(relation.getDeparture());
                        result.setArrival(relation.getArrival());
                        result.setDepartureFlag(relation.getDepartureFlag());
                        result.setArrivalFlag(relation.getArrivalFlag());

                        long daysArrived = calculateDaysArrived(relation.getDepartureTime(), relation.getArrivalTime());
                        result.setDaysArrived((int) daysArrived);

                        Date now = new Date();
                        result.setSaleStatus(now.after(trainDO.getSaleTime()) ? 0 : 1);
                        result.setSaleTime(formatTime(trainDO.getSaleTime(), "MM-dd HH:mm"));

                        ticketList.add(result);

                        try {
                            String cacheKey = String.format("%s_%s_%s", 
                            trainDO.getId(), relation.getDeparture(), relation.getArrival());
                            trainStationMap.put(cacheKey, objectMapper.writeValueAsString(result));
                        } catch (JsonProcessingException e) {
                            logger.error("Failed to serialize ticket info", e);
                        }
                    }

                    if(!trainStationMap.isEmpty()) {
                        stringRedisTemplate.opsForHash().putAll(trainStationCacheKey, trainStationMap);
                        stringRedisTemplate.expire(trainStationCacheKey, RailwayConstant.ADVANCE_TICKET_DAY, TimeUnit.DAYS);
                    }
                }
            } finally {
                lock.unlock();
            }
        }

        if (ticketList.isEmpty() && !trainStationMap.isEmpty()) {
            ticketList = trainStationMap.values().stream()
                .map(value -> {
                    try {
                        return objectMapper.readValue((String) value, TicketListDTO.class);
                    } catch (Exception e) {
                        logger.error("Failed to deserialize ticket info", e);
                        return null;
                    }
                })
                .filter(ticket -> ticket != null)
                .collect(Collectors.toList());
        }

        ticketList.sort(Comparator.comparing(TicketListDTO::getDepartureTime));

        // 查询每个列车的座位和票价信息
    for (TicketListDTO ticket : ticketList) {
        // 从缓存获取票价信息
        String trainStationPriceKey = String.format(RedisKeyConstant.TRAIN_STATION_PRICE, 
                ticket.getTrainId(), ticket.getDeparture(), ticket.getArrival());
        
        String trainStationPriceStr = distributedCache.safeGet(
                trainStationPriceKey,
                String.class,
                () -> {
                    // 缓存未命中时查询数据库
                    List<TrainStationPriceDO> priceList = trainStationPriceMapper.selectByTrainAndStations(
                            Long.parseLong(ticket.getTrainId()), 
                            ticket.getDeparture(), 
                            ticket.getArrival());
                    
                    try {
                        return objectMapper.writeValueAsString(priceList);
                    } catch (JsonProcessingException e) {
                        logger.error("Failed to serialize price list for train: {}", ticket.getTrainId(), e);
                        return "[]";
                    }
                },
                RailwayConstant.ADVANCE_TICKET_DAY,
                TimeUnit.DAYS
        );
        
        // 解析票价信息
        List<TrainStationPriceDO> priceList;
        try {
            priceList = objectMapper.readValue(trainStationPriceStr,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, TrainStationPriceDO.class));
        } catch (Exception e) {
            logger.error("Failed to deserialize price list for train: {}", ticket.getTrainId(), e);
            priceList = Collections.emptyList();
        }
        
        // 构建座位类型信息
        List<SeatClassDTO> seatClassList = new ArrayList<>();
        for (TrainStationPriceDO price : priceList) {
            String seatType = String.valueOf(price.getSeatType());
            String keySuffix = String.join("_", ticket.getTrainId(), price.getDeparture(), price.getArrival());
            
            // 查询余票
            Object quantityObj = stringRedisTemplate.opsForHash().get(
                    RedisKeyConstant.TRAIN_STATION_REMAINING_TICKET + keySuffix, seatType);
            // 添加调试日志
            log.info("Redis查询 Key: {}", RedisKeyConstant.TRAIN_STATION_REMAINING_TICKET + keySuffix);
            log.info("Redis查询 Field: {}", seatType);
            log.info("Redis查询结果 quantityObj: {}", quantityObj);
            // 解析余票数量
            int quantity = Optional.ofNullable(quantityObj)
                    .map(Object::toString)
                    .map(Integer::parseInt)
                    .orElseGet(() -> {
                        // 添加日志，确认是否进入这个方法
                        logger.info("进入 cacheLoader 加载方法");

                        // 缓存未命中，使用座位缓存加载器加载
                        Map<String, String> seatMarginMap = seatMarginCacheLoader.load(
                                ticket.getTrainId(), 
                                seatType, 
                                price.getDeparture(), 
                                price.getArrival());
                        
                        return Optional.ofNullable(seatMarginMap.get(seatType))
                                .map(Integer::parseInt)
                                .orElse(0);
                    });
            
            // 构建座位类型DTO，价格单位转换（分->元）
            BigDecimal priceValue = new BigDecimal(price.getPrice())
                    .divide(new BigDecimal("100"), 1, RoundingMode.HALF_UP);
            
            SeatClassDTO seatClassDTO = new SeatClassDTO();
            seatClassDTO.setType(price.getSeatType());
            seatClassDTO.setQuantity(quantity);
            seatClassDTO.setPrice(priceValue);
            seatClassList.add(seatClassDTO);
        }
        
        // 设置座位类型列表
        ticket.setSeatClassList(seatClassList);
    }

    // 构建并返回最终响应结果
    return TicketPageQueryRespDTO.builder()
            .trainList(ticketList)
            .departureStationList(buildDepartureStationList(ticketList))
            .arrivalStationList(buildArrivalStationList(ticketList))
            .seatClassTypeList(buildSeatClassList(ticketList))
            .build();        
    }

    
   
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public TicketPurchaseRespDTO executePurchaseTickets(PurchaseTicketReqDTO requestParam) throws ServiceException {

        String authHeader = request.getHeader("Authorization");
        String userId1 = null;
    
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        if (JWTUtil.validateToken(token)) {
            // 从JWT中提取用户ID
            userId1 = JWTUtil.getUserIdFromToken(token);
            
            // 将用户ID设置到requestParam的passengerId字段
            requestParam.setPassengerId(userId1);
        } else {
            throw new ServiceException("Invalid token");
        }
    } else {
        throw new ServiceException("Authorization token is required");
    }

        List<TicketOrderDetailRespDTO> ticketOrderDetailResults = new ArrayList<>();
        String trainId = requestParam.getTrainId();
        
        // Get train information from cache or database
        TrainDO trainDO = distributedCache.safeGet(
                RedisKeyConstant.TRAIN_INFO + trainId,
                TrainDO.class,
                () -> trainMapper.selectById(Long.parseLong(trainId)),
                RailwayConstant.ADVANCE_TICKET_DAY,
                TimeUnit.DAYS);
        
        if (trainDO == null) {
            throw new ServiceException("Train not found with ID: " + trainId);
        }
        
        // Select seats for this ticket purchase
        List<TrainPurchaseTicketRespDTO> selectedSeats;
        try {
            selectedSeats = trainSeatTypeSelector.select(requestParam);
        } catch (Exception e) {
            throw new ServiceException("Failed to select seats: " + e.getMessage(), e);
        }
        
        // Transform selected seats into ticket records
        List<TicketDO> ticketDOList = new ArrayList<>();
        for (TrainPurchaseTicketRespDTO seat : selectedSeats) {
            TicketDO ticketDO = TicketDO.builder()
                    // .username(requestParam.getUsername())
                    .trainId(Long.parseLong(requestParam.getTrainId()))
                    .carriageNumber(seat.getCarriageNumber())
                    .seatNumber(seat.getSeatNumber())
                    .passengerId(seat.getPassengerId())
                    .ticketStatus(TicketStatusEnum.UNPAID.getCode())
                    .build();
            ticketDO.setCreateTime(new Date());
            ticketDO.setUpdateTime(new Date());
            
            ticketDOList.add(ticketDO);
            

        }
        
        // Create order request
        // TicketOrderCreateReqDTO orderCreateReq = buildOrderCreateRequest(requestParam, trainDO, selectedSeats);
        
        TicketOrderCreateReqDTO orderCreateReq = new TicketOrderCreateReqDTO();
        orderCreateReq.setTrainId(Long.parseLong(requestParam.getTrainId()));
        orderCreateReq.setTrainNumber(trainDO.getTrainNumber());
        orderCreateReq.setDeparture(requestParam.getDeparture());
        orderCreateReq.setArrival(requestParam.getArrival());
        orderCreateReq.setRidingDate(new Date()); // 可能需要根据实际情况调整
        orderCreateReq.setDepartureTime(trainDO.getDepartureTime());
        orderCreateReq.setArrivalTime(trainDO.getArrivalTime());
        
        // Set seat details
        Long userId = Long.parseLong(requestParam.getPassengerId());
        UserDO user = userMapper.selectById(userId);
        
        // 在创建userid前添加非空检查
        log.info("UserId before null check: {}", userId);
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null for sharding");
        }
        if (user != null) {
            orderCreateReq.setUserId(userId);
            orderCreateReq.setUsername(user.getUsername());
        }

        // Create order in OrderService
        String orderSn = orderService.createTicketOrder(orderCreateReq);
        
        if (orderSn == null || orderSn.isEmpty()) {
            throw new ServiceException("Failed to create order");
        }
        
        // Query the created order details
        TicketOrderDetailRespDTO orderDetail = orderService.queryTicketOrderByOrderSn(orderSn);
        if (orderDetail != null) {
            ticketOrderDetailResults.add(orderDetail);
        }
        
        // Return purchase result with order number and details
        return new TicketPurchaseRespDTO(orderSn, ticketOrderDetailResults);
    }


    public TicketPurchaseRespDTO purchaseTicket(PurchaseTicketReqDTO requestParam) throws ServiceException {

        String trainId = requestParam.getTrainId();
        if (trainId == null) {
            throw new IllegalArgumentException("Train ID cannot be null");
        }
        log.info("LOCK_PURCHASE_TICKETS value: '{}'", RedisKeyConstant.LOCK_PURCHASE_TICKETS);
        String lockKey = environment.resolvePlaceholders(String.format(RedisKeyConstant.LOCK_PURCHASE_TICKETS, requestParam.getTrainId()));
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        try {
            return executePurchaseTickets(requestParam);
        } catch (ServiceException e) {
            throw new RuntimeException("Failed to purchase ticket: " + e.getMessage(), e);
        } finally {
            lock.unlock();
        } 
    }

    private String formatTime(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    private static String calculateHourDifference(Date startTime, Date endTime) {
        if (startTime == null || endTime == null) {
            return "";
        }

        long diffMillis = endTime.getTime() - startTime.getTime();
        if (diffMillis < 0) {
            return "";  
        }

        long hours = diffMillis / (60 * 60 * 1000);
        long minutes = (diffMillis % (60 * 60 * 1000)) / (60 * 1000);
        
        StringBuilder duration = new StringBuilder();
        if (hours > 0) {
            duration.append(hours).append("h");
        }
        if (minutes > 0 || hours == 0) {
            duration.append(minutes).append("min");
        }
        
        return duration.toString();
    }
    
    private int calculateDaysArrived(Date departureTime, Date arrivalTime) {
        if (departureTime == null || arrivalTime == null) {
            return 0;
        }
        
        // 创建日历实例
        Calendar depCal = Calendar.getInstance();
        depCal.setTime(departureTime);
        
        Calendar arrCal = Calendar.getInstance();
        arrCal.setTime(arrivalTime);
        
        // 获取日期部分（年、月、日）
        int depYear = depCal.get(Calendar.YEAR);
        int depMonth = depCal.get(Calendar.MONTH);
        int depDay = depCal.get(Calendar.DAY_OF_MONTH);
        
        int arrYear = arrCal.get(Calendar.YEAR);
        int arrMonth = arrCal.get(Calendar.MONTH);
        int arrDay = arrCal.get(Calendar.DAY_OF_MONTH);
        
        // 重置时间部分，只保留日期
        depCal.clear();
        depCal.set(depYear, depMonth, depDay);
        
        arrCal.clear();
        arrCal.set(arrYear, arrMonth, arrDay);
        
        // 计算日期差（毫秒）
        long diffMillis = arrCal.getTimeInMillis() - depCal.getTimeInMillis();
        
        // 转换为天数
        return (int)(diffMillis / (24 * 60 * 60 * 1000));
    }

    private List<String> buildDepartureStationList(List<TicketListDTO> seatResults) {
        return seatResults.stream().map(TicketListDTO::getDeparture).distinct().collect(Collectors.toList());
    }

    private List<String> buildArrivalStationList(List<TicketListDTO> seatResults) {
        return seatResults.stream().map(TicketListDTO::getArrival).distinct().collect(Collectors.toList());
    }

    private List<Integer> buildSeatClassList(List<TicketListDTO> seatResults) {
        Set<Integer> resultSeatClassList = new HashSet<>();
        for (TicketListDTO each : seatResults) {
            for (SeatClassDTO item : each.getSeatClassList()) {
                resultSeatClassList.add(item.getType());
            }
        }
        return resultSeatClassList.stream().toList();
    }
}
