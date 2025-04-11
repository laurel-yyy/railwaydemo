package com.ourgroup.railway.framework.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SeatStatusEnum {

    /**
     * 可售
     */
    AVAILABLE(0),

    /**
     * 锁定
     */
    LOCKED(1),

    /**
     * 已售
     */
    SOLD(2);

    @Getter
    private final Integer code;
}
