package com.ourgroup.railway.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ourgroup.railway.framework.toolkit.StationCalculateUtil;
import com.ourgroup.railway.mapper.TrainStationMapper;
import com.ourgroup.railway.model.dao.TrainStationDO;
import com.ourgroup.railway.model.dto.domain.RouteDTO;
import com.ourgroup.railway.service.TrainStationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainStationServiceImpl implements TrainStationService {

    private final TrainStationMapper trainStationMapper;


    @Override
    public List<RouteDTO> listTrainStationRoute(String trainId, String departure, String arrival) {

        List<String> trainStationList = trainStationMapper.selectStationsByTrainId(Long.parseLong(trainId));
        
        return StationCalculateUtil.throughStation(trainStationList, departure, arrival);
    }

    @Override
    public List<RouteDTO> listTakeoutTrainStationRoute(String trainId, String departure, String arrival) {

        List<String> trainStationAllList = trainStationMapper.selectStationsByTrainId(Long.parseLong(trainId));

        return StationCalculateUtil.takeoutStation(trainStationAllList, departure, arrival);    
    }

}