package com.ourgroup.railway.service.handler;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import com.ourgroup.railway.framework.cache.DistributedCache;
import com.ourgroup.railway.framework.toolkit.SeatNumberUtil;
import com.ourgroup.railway.model.dto.domain.RouteDTO;
import com.ourgroup.railway.model.dto.req.PurchaseTicketReqDTO;
import com.ourgroup.railway.model.dto.resp.TrainPurchaseTicketRespDTO;
import com.ourgroup.railway.service.SeatService;
import com.ourgroup.railway.service.TrainStationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TrainPurchaseTicketHandler {

    private final SeatService seatService;
    private final TrainStationService trainStationService;
    private final DistributedCache distributedCache;
    private final ConfigurableEnvironment environment;

    private String ticketAvailabilityCacheUpdateType;

    private static final Map<Character, Integer> SEAT_Y_INT = Map.of('A', 0, 'B', 1, 'C', 2, 'D', 3, 'F', 4);
    private static final String TRAIN_STATION_REMAINING_TICKET = "railway:train:remaining:";

    public void run() {
        ticketAvailabilityCacheUpdateType = environment.getProperty("ticket.availability.cache-update.type", "");
    }

    public TrainPurchaseTicketRespDTO executeResp(PurchaseTicketReqDTO requestParam) throws Exception {
        TrainPurchaseTicketRespDTO actualResult = selectSeat(requestParam);
        
        if (actualResult != null && !Objects.equals(ticketAvailabilityCacheUpdateType, "binlog")) {
            String trainId = requestParam.getTrainId();
            String departure = requestParam.getDeparture();
            String arrival = requestParam.getArrival();
            StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
            List<RouteDTO> routeDTOList = trainStationService.listTakeoutTrainStationRoute(trainId, departure, arrival);
            routeDTOList.forEach(each -> {
                String keySuffix = String.join("_", trainId, each.getStartStation(), each.getEndStation());
                stringRedisTemplate.opsForHash().increment(TRAIN_STATION_REMAINING_TICKET + keySuffix, 
                    String.valueOf(requestParam.getSeatType()), 
                    -1);
            });
            String keySuffix = String.join("_", trainId, departure, arrival);
            stringRedisTemplate.opsForHash().increment(TRAIN_STATION_REMAINING_TICKET + keySuffix, 
                String.valueOf(requestParam.getSeatType()), 
                +1);
        }
        return actualResult;
    }

    public TrainPurchaseTicketRespDTO selectSeat(PurchaseTicketReqDTO requestParam) throws Exception {
        String trainId = requestParam.getTrainId();
        String departure = requestParam.getDeparture();
        String arrival = requestParam.getArrival();
        String passengerId = requestParam.getPassengerId();
        Integer seatType = requestParam.getSeatType();
        
        List<String> trainCarriageList = seatService.listUsableCarriageNumber(trainId, seatType, departure, arrival);
        List<Integer> trainStationCarriageRemainingTicket = seatService.listSeatRemainingTicket(trainId, departure, arrival, trainCarriageList);
        System.out.println("可用车厢列表:");
        for (String carriage : trainCarriageList) {
            System.out.print(carriage + " ");
        }
        System.out.println();
        
        System.out.println("各车厢剩余票数:");
        for (int i = 0; i < trainCarriageList.size(); i++) {
            System.out.println("车厢 " + trainCarriageList.get(i) + 
                              ": " + trainStationCarriageRemainingTicket.get(i) + " 张票");
        }
        int remainingTicketSum = trainStationCarriageRemainingTicket.stream().mapToInt(Integer::intValue).sum();
        if (remainingTicketSum < 1) {
            throw new Exception("站点余票不足，请尝试更换座位类型或选择其它站点");
        }

        // If a specific seat is chosen, try to find that
        if (requestParam.getChooseSeat() != null && !requestParam.getChooseSeat().trim().isEmpty()) {
            TrainPurchaseTicketRespDTO matchedSeat = findMatchSeat(requestParam, trainCarriageList, trainStationCarriageRemainingTicket);
            if (matchedSeat != null) {
                return matchedSeat;
            }
        }

        // Otherwise, select a seat automatically
        return selectSingleSeat(requestParam, trainCarriageList, trainStationCarriageRemainingTicket);
    }

    private TrainPurchaseTicketRespDTO findMatchSeat(PurchaseTicketReqDTO requestParam, List<String> trainCarriageList, List<Integer> trainStationCarriageRemainingTicket) {
        for (int i = 0; i < trainStationCarriageRemainingTicket.size(); i++) {
            String carriagesNumber = trainCarriageList.get(i);
            List<String> listAvailableSeat = seatService.listAvailableSeat(
                requestParam.getTrainId(), 
                carriagesNumber, 
                requestParam.getSeatType(), 
                requestParam.getDeparture(), 
                requestParam.getArrival()
            );
            
            int[][] actualSeats = new int[18][5];
            for (int j = 1; j < 19; j++) {
                for (int k = 1; k < 6; k++) {
                    if (j <= 9) {
                        actualSeats[j - 1][k - 1] = listAvailableSeat.contains("0" + j + SeatNumberUtil.convert(k)) ? 0 : 1;
                    } else {
                        actualSeats[j - 1][k - 1] = listAvailableSeat.contains("" + j + SeatNumberUtil.convert(k)) ? 0 : 1;
                    }
                }
            }
            
            // Check if the chosen seat is available
            Pair<Integer, Integer> chooseSeatCoordinate = calcChooseSeatCoordinate(actualSeats, requestParam.getChooseSeat());
            
            if (chooseSeatCoordinate != null) {
                String seatNumber = requestParam.getChooseSeat();
                
                return TrainPurchaseTicketRespDTO.builder()
                        .seatNumber(seatNumber)
                        .seatType(requestParam.getSeatType())
                        .carriageNumber(carriagesNumber)
                        .passengerId(requestParam.getPassengerId())
                        .build();
            }
        }
        
        return null;
    }

    private TrainPurchaseTicketRespDTO selectSingleSeat(PurchaseTicketReqDTO requestParam, List<String> trainCarriageList, List<Integer> trainStationCarriageRemainingTicket) throws Exception {
        for (int i = 0; i < trainStationCarriageRemainingTicket.size(); i++) {
            String carriagesNumber = trainCarriageList.get(i);
            List<String> listAvailableSeat = seatService.listAvailableSeat(
                requestParam.getTrainId(), 
                carriagesNumber, 
                requestParam.getSeatType(), 
                requestParam.getDeparture(), 
                requestParam.getArrival()
            );
            
            int[][] actualSeats = new int[18][5];
            for (int j = 1; j < 19; j++) {
                for (int k = 1; k < 6; k++) {
                    if (j <= 9) {
                        actualSeats[j - 1][k - 1] = listAvailableSeat.contains("0" + j + SeatNumberUtil.convert(k)) ? 0 : 1;
                    } else {
                        actualSeats[j - 1][k - 1] = listAvailableSeat.contains("" + j + SeatNumberUtil.convert(k)) ? 0 : 1;
                    }
                }
            }
            
            // Try to find the first available seat
            for (int row = 0; row < 18; row++) {
                for (int col = 0; col < 5; col++) {
                    if (actualSeats[row][col] == 0) {
                        String seatNumber = (row <= 8 ? "0" : "") + (row + 1) 
                            + SeatNumberUtil.convert(col + 1);
                        
                            return TrainPurchaseTicketRespDTO.builder()
                                    .seatNumber(seatNumber)
                                    .seatType(requestParam.getSeatType())
                                    .carriageNumber(carriagesNumber)
                                    .passengerId(requestParam.getPassengerId())
                                    .build();
                    }
                }
            }
        }
        
        throw new Exception("无法找到可用座位");
    }

    private Pair<Integer, Integer> calcChooseSeatCoordinate(int[][] actualSeats, String chooseSeat) {
        if (chooseSeat == null || chooseSeat.trim().isEmpty()) {
            return null;
        }
        
        int seatX = Integer.parseInt(chooseSeat.substring(1)) - 1;
        int seatY = SEAT_Y_INT.get(chooseSeat.charAt(0));
        
        if (actualSeats[seatX][seatY] == 0) {
            return Pair.of(seatX, seatY);
        }
        
        return null;
    }
}