package com.icfolson.aem.library.core.jmx;

import com.adobe.granite.jmx.annotation.Description;
import com.adobe.granite.jmx.annotation.Name;

import javax.management.openmbean.TabularDataSupport;

@Description("Google Guava Cache Reporting and Maintenance")
public interface GoogleGuavaCacheReportingAndMaintenanceMBean {

    /**
     * Clear all guava caches within all reporting caching services.
     */
    @Description("Clear all guava caches within all reporting caching services")
    void clearAllCaches();

    /**
     * Clear all guava caches within a specific cache service.
     *
     * @param cacheService cache service name
     */
    @Description("Clear all guava caches within a specific cache service")
    void clearAllCachesForService(@Name("cacheService") @Description(
        "The fully qualified path of a cache service listed in the Registered Cache Services") String cacheService);

    /**
     * Clear a specific guava cache within a specific cache service.
     *
     * @param cacheService cache service name
     * @param cacheKey cache key
     */
    @Description("Clear a specific guava cache within a specific cache service")
    void clearSpecificCacheForSpecificService(@Name("cacheService") @Description(
        "The fully qualified path of a cache service listed in the Registered Cache Services") String cacheService,
        @Name("cacheKey") @Description("The cache key listed in the exposed caches") String cacheKey);

    /**
     * Lists all cache statistics for all caches exposed by all reporting cache services.
     *
     * @return all cache statistics for all caches exposed by all reporting cache services
     */
    @Description("Lists all cache statistics for all caches exposed by all reporting cache services")
    TabularDataSupport getCacheStats();

    /**
     * Lists all caches exposed by all reporting cache services.
     *
     * @return all caches exposed by all reporting cache services
     */
    @Description("Lists all caches exposed by all reporting cache services")
    TabularDataSupport getExposedCaches();

    /**
     * Lists all registered cache services.
     *
     * @return all registered cache services
     */
    @Description("Lists all registered cache services")
    TabularDataSupport getRegisteredCacheServices();
}