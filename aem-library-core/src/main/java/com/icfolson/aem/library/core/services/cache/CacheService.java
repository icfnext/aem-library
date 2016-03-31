package com.icfolson.aem.library.core.services.cache;

import com.google.common.cache.CacheStats;

import java.util.List;

public interface CacheService {

    /**
     * Clear all caches.
     *
     * @return true if caches are cleared
     */
    boolean clearAllCaches();

    /**
     * @param cacheVariableName cache name
     * @return true if cache is cleared
     */
    boolean clearSpecificCache(String cacheVariableName);

    /**
     * @param cacheVariableName cache name
     * @return cache size
     */
    Long getCacheSize(String cacheVariableName);

    /**
     * @param cacheVariableName cache name
     * @return cache stats
     */
    CacheStats getCacheStats(String cacheVariableName);

    /**
     * @return list of cache names
     */
    List<String> listCaches();
}