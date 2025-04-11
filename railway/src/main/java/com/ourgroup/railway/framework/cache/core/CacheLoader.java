package com.ourgroup.railway.framework.cache.core;

@FunctionalInterface
public interface CacheLoader<T> {

    /**
     * 加载缓存
     */
    T load();
}
