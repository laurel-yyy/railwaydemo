package com.ourgroup.railway.framework.toolkit;

import java.util.HashMap;
import java.util.Map;

/**
 * 座位号转换工具
 */
public final class SeatNumberUtil {

    private static final Map<Integer, String> TRAIN_SEAT_NUMBER_MAP = new HashMap<>();

    static {
        TRAIN_SEAT_NUMBER_MAP.put(1, "A");
        TRAIN_SEAT_NUMBER_MAP.put(2, "B");
        TRAIN_SEAT_NUMBER_MAP.put(3, "C");
        TRAIN_SEAT_NUMBER_MAP.put(4, "D");
        TRAIN_SEAT_NUMBER_MAP.put(5, "F");
    }

    /**
     * 根据类型转换座位号
     *
     * @param type 列车座位类型
     * @param num  座位号
     * @return 座位编号
     */
    public static String convert(int num) {
        String serialNumber =  TRAIN_SEAT_NUMBER_MAP.get(num);
        return serialNumber;
    }
}
