package com.ourgroup.railway.model.dao;

import lombok.Data;
import com.ourgroup.railway.model.dao.BaseDO;


@Data
public class StationDO extends BaseDO {

    /**
     * id
     */
    private Long id;

    /**
     * 车站编码
     */
    private String code;

    /**
     * 车站名称
     */
    private String name;

    /**
     * 地区编号
     */
    private String region;

    /**
     * 地区名称
     */
    private String regionName;
}
