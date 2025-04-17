
package com.ourgroup.railway.framework.cache.core;

@FunctionalInterface
public interface CacheGetIfAbsent<T> {

    void execute(T param);
}
