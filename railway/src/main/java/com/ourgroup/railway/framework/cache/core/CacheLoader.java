package com.ourgroup.railway.framework.cache.core;

@FunctionalInterface
public interface CacheLoader<T> {

    T load();
}
