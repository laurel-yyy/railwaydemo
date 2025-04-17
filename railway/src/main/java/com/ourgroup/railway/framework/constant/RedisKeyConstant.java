package com.ourgroup.railway.framework.constant;

public class RedisKeyConstant {

    public static final String TRAIN_INFO = "railway:train:info:";
    

    public static final String TRAIN_STATIONS = "railway:train:stations:%s:%s";
    

    public static final String TRAIN_STATION_PRICE = "railway:train:price:%s:%s:%s";
    

    public static final String TRAIN_STATION_REMAINING_TICKET = "railway:train:remaining:";


    public static final String TRAIN_STATION_CARRIAGE_REMAINING_TICKET = "railway:train:carriage:remaining:";

    public static final String LOCK_TRAIN_STATION_CARRIAGE_REMAINING_TICKET = "railway:lock:train:carriage:remaining:";
    

    public static final String LOCK_TRAIN_STATIONS = "railway:lock:train:stations";

    public static final String LOCK_SAFE_LOAD_SEAT_MARGIN_GET = "railway:lock:safe_load_seat_margin_%s";
    
    public static final String LOCK_PURCHASE_TICKETS = "railway:lock:purchase_tickets:%s";
}