package com.ourgroup.railway.framework.cache;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.util.Collection;

public interface Cache {

    <T> T get(@NotBlank String key, Class<T> clazz);

    void put(@NotBlank String key, Object value);

    Boolean putIfAllAbsent(@NotNull Collection<String> keys);

    Boolean delete(@NotBlank String key);

    Long delete(@NotNull Collection<String> keys);

    Boolean hasKey(@NotBlank String key);

    Object getInstance();
}
