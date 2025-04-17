package com.ourgroup.railway.framework.toolkit;

import java.util.HashMap;
import java.util.Map;

/**
 * Seat Number Convert Util
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
     * Convert seat number to serial number.
     */
    public static String convert(int num) {
        String serialNumber =  TRAIN_SEAT_NUMBER_MAP.get(num);
        return serialNumber;
    }
}
