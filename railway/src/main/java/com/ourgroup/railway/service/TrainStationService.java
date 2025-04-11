package com.ourgroup.railway.service;

import java.util.List;

import com.ourgroup.railway.model.dto.domain.RouteDTO;

public interface TrainStationService {

    List<RouteDTO> listTrainStationRoute(String trainId, String startStation, String endStation);

    List<RouteDTO> listTakeoutTrainStationRoute(String trainId, String departure, String arrival);
    
} 