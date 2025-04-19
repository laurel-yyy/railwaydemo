package com.ourgroup.railway.framework.cache.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;


@Data
@ConfigurationProperties(prefix = BloomFilterPenetrateProperties.PREFIX)
public class BloomFilterPenetrateProperties {

    public static final String PREFIX = "framework.cache.redis.bloom-filter.default";

    private String name = "cache_penetration_bloom_filter";

    private Long expectedInsertions = 64L;

    private Double falseProbability = 0.03D;
}
