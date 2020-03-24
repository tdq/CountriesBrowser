package org.nikolay.broadcom.services;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Not thread save caching mechanism
 * @param <TYPE>
 */
class Cache<TYPE> {

    private static final int DEFAULT_CACHE_LIVE_TIME_SECONDS = 3600;

    // TODO can be wrapped by weak link
    private TYPE value;
    private Instant valueTimestamp = Instant.EPOCH;
    private final Supplier<TYPE> valueSuplier;
    private final int cacheLiveTimeSeconds;

    Cache(@NotNull Supplier<TYPE> valueSuplier) {
        this(valueSuplier, DEFAULT_CACHE_LIVE_TIME_SECONDS);
    }

    Cache(@NotNull Supplier<TYPE> valueSuplier, int cacheLiveTime) {
        if(cacheLiveTime < 0) {
            throw new IllegalArgumentException("Cache live time can't be negative");
        }

        this.valueSuplier = Objects.requireNonNull(valueSuplier, "Suplier can't be null");
        this.cacheLiveTimeSeconds = cacheLiveTime;
    }

    @NotNull
    TYPE getValue() {
        Instant currentTime = Instant.now();

        if(valueTimestamp.plusSeconds(cacheLiveTimeSeconds).isBefore(currentTime)) {
            value = valueSuplier.get();
            valueTimestamp = currentTime;
        }

        return value;
    }
}
