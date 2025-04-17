package com.ourgroup.railway.framework.cache.core;


@FunctionalInterface
public interface CacheGetFilter<T> {

    boolean filter(T param);
}
