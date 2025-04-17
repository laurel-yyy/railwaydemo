package com.ourgroup.railway.framework.cache.config;

import lombok.Data;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = RedisDistributedProperties.PREFIX)
public class RedisDistributedProperties {

    public static final String PREFIX = "framework.cache.redis";

    private String prefix = "";

    private String prefixCharset = "UTF-8";

    private Long valueTimeout = 30000L;

    private TimeUnit valueTimeUnit = TimeUnit.MILLISECONDS;
}
