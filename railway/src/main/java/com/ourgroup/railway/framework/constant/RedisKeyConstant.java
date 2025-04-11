package com.ourgroup.railway.framework.constant;

public class RedisKeyConstant {
    /**
     * 列车基本信息
     */
    public static final String TRAIN_INFO = "railway:train:info:";
    
    /**
     * 列车站点缓存
     */
    public static final String TRAIN_STATIONS = "railway:train:stations:%s:%s";
    
    /**
     * 列车站点票价
     */
    public static final String TRAIN_STATION_PRICE = "railway:train:price:%s:%s:%s";
    
    /**
     * 列车站点余票
     */
    public static final String TRAIN_STATION_REMAINING_TICKET = "railway:train:remaining:";

    /**
     * 列车车厢余票
     */
    public static final String TRAIN_STATION_CARRIAGE_REMAINING_TICKET = "railway:train:carriage:remaining:";
    
    /**
     * 列车站点锁
     */
    public static final String LOCK_TRAIN_STATIONS = "railway:lock:train:stations";

    /**
     * 获取相邻座位余票分布式锁 Key
     */
    public static final String LOCK_SAFE_LOAD_SEAT_MARGIN_GET = "railway:lock:safe_load_seat_margin_%s";

    public static final String LOCK_PURCHASE_TICKETS = null;
}