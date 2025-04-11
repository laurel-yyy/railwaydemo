package com.ourgroup.railway.model.dao;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class BaseDO {

    private Date createTime;

    private Date updateTime;

    private Integer delFlag;
}
