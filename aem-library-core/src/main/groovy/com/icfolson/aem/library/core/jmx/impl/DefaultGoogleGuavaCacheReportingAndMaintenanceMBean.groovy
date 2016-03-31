package com.icfolson.aem.library.core.jmx.impl

import com.adobe.granite.jmx.annotation.AnnotatedStandardMBean
import com.icfolson.aem.library.core.services.cache.CacheService
import com.google.common.collect.Lists
import com.icfolson.aem.library.core.jmx.GoogleGuavaCacheReportingAndMaintenanceMBean
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.ReferenceCardinality
import org.apache.felix.scr.annotations.ReferencePolicy
import org.apache.felix.scr.annotations.Service

import javax.management.NotCompliantMBeanException
import javax.management.openmbean.CompositeDataSupport
import javax.management.openmbean.CompositeType
import javax.management.openmbean.OpenType
import javax.management.openmbean.SimpleType
import javax.management.openmbean.TabularDataSupport
import javax.management.openmbean.TabularType
import java.math.RoundingMode
import java.util.concurrent.TimeUnit

@Component
@Property(name = "jmx.objectname", value = "com.icfolson.aem.library:type=Google Guava Cache Reporting and Maintenance")
@Service(GoogleGuavaCacheReportingAndMaintenanceMBean)
@Slf4j("LOG")
class DefaultGoogleGuavaCacheReportingAndMaintenanceMBean extends AnnotatedStandardMBean implements
    GoogleGuavaCacheReportingAndMaintenanceMBean {

    @Reference(cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC,
        referenceInterface = CacheService, bind = "bindCacheService", unbind = "unbindCacheService")
    private final List<CacheService> cacheServices = Lists.newCopyOnWriteArrayList()

    public DefaultGoogleGuavaCacheReportingAndMaintenanceMBean() throws NotCompliantMBeanException {
        super(GoogleGuavaCacheReportingAndMaintenanceMBean)
    }

    @Override
    void clearAllCaches() {
        cacheServices*.clearAllCaches()
    }

    @Override
    void clearAllCachesForService(String cacheServiceClassName) {
        cacheServices.each { cacheService ->
            if (StringUtils.equalsIgnoreCase(cacheService.class.name, cacheServiceClassName)) {
                cacheService.clearAllCaches()
            }
        }
    }

    @Override
    void clearSpecificCacheForSpecificService(String cacheServiceClassName, String cacheKey) {
        cacheServices.each { cacheService ->
            if (StringUtils.equalsIgnoreCase(cacheService.class.name, cacheServiceClassName)) {
                cacheService.clearSpecificCache(cacheKey)
            }
        }
    }

    @Override
    TabularDataSupport getCacheStats() {
        TabularDataSupport tabularDataSupport = null

        try {
            def itemNamesAndDescriptions = ["Cache Service", "Cache Key", "Average Load Penalty", "Eviction Count",
                                            "Hit Count", "% Hit Rate", "Load Count", "Load Exception Count",
                                            "% Load Exception Rate", "Load Success Count", "Miss Count",
                                            "% Miss Rate", "Request Count", "Total Load Time (s)",
                                            "Cache Size"] as String[]
            def itemTypes = [SimpleType.STRING, SimpleType.STRING, SimpleType.DOUBLE, SimpleType.LONG,
                             SimpleType.LONG, SimpleType.BIGDECIMAL, SimpleType.LONG, SimpleType.LONG,
                             SimpleType.BIGDECIMAL, SimpleType.LONG, SimpleType.LONG, SimpleType.BIGDECIMAL,
                             SimpleType.LONG, SimpleType.LONG, SimpleType.LONG] as OpenType[]
            def indexNames = ["Cache Service", "Cache Key"] as String[]

            def pageType = new CompositeType("page", "Page size info", itemNamesAndDescriptions,
                itemNamesAndDescriptions, itemTypes)
            def pageTabularType = new TabularType("List of Caches and Statistics", "List of Caches and Statistics",
                pageType, indexNames)

            tabularDataSupport = new TabularDataSupport(pageTabularType)

            cacheServices.each { cacheService ->
                def cacheServiceClassName = cacheService.class.name

                cacheService.listCaches().each { cacheName ->
                    def cacheStats = cacheService.getCacheStats(cacheName)
                    def cacheSize = cacheService.getCacheSize(cacheName)

                    if (cacheStats) {
                        def hitRate = new BigDecimal(cacheStats.hitRate()).setScale(2,
                            RoundingMode.HALF_UP).movePointRight(2)
                        def loadExceptionRate = new BigDecimal(cacheStats.loadExceptionRate()).setScale(2,
                            RoundingMode.HALF_UP).movePointRight(2)
                        def missRate = new BigDecimal(cacheStats.missRate()).setScale(2,
                            RoundingMode.HALF_UP).movePointRight(2)
                        def loadTimeInSeconds = TimeUnit.SECONDS.convert(cacheStats.totalLoadTime(),
                            TimeUnit.NANOSECONDS)

                        tabularDataSupport.put(new CompositeDataSupport(pageType, itemNamesAndDescriptions,
                            [cacheServiceClassName, cacheName, cacheStats.averageLoadPenalty(),
                             cacheStats.evictionCount(), cacheStats.hitCount(), hitRate, cacheStats.loadCount(),
                             cacheStats.loadExceptionCount(), loadExceptionRate, cacheStats.loadSuccessCount(),
                             cacheStats.missCount(), missRate, cacheStats.requestCount(), loadTimeInSeconds,
                             cacheSize] as Object[]))
                    }
                }
            }
        } catch (exception) {
            LOG.error "An exception occurred building tabulardata for cache stats.", exception
        }

        tabularDataSupport
    }

    @Override
    TabularDataSupport getExposedCaches() {
        TabularDataSupport tabularDataSupport = null

        try {
            def itemNamesAndDescriptions = ["Cache Service", "Cache Key"] as String[]
            def itemTypes = [SimpleType.STRING, SimpleType.STRING] as OpenType[]
            def indexNames = ["Cache Service", "Cache Key"] as String[]

            def pageType = new CompositeType("page", "Page size info", itemNamesAndDescriptions,
                itemNamesAndDescriptions, itemTypes)
            def pageTabularType = new TabularType("List of Cache Services and Keys",
                "List of Cache Services and Keys", pageType, indexNames)

            tabularDataSupport = new TabularDataSupport(pageTabularType)

            cacheServices.each { cacheService ->
                def cacheServiceClassName = cacheService.class.name

                cacheService.listCaches().each { cacheName ->
                    tabularDataSupport.put(new CompositeDataSupport(pageType, itemNamesAndDescriptions,
                        [cacheServiceClassName, cacheName] as Object[]))
                }
            }
        } catch (exception) {
            LOG.error "An exception occurred building tabulardata for the exposed caches.", exception
        }

        tabularDataSupport
    }

    @Override
    TabularDataSupport getRegisteredCacheServices() {
        TabularDataSupport tabularDataSupport = null

        def itemNamesAndDescriptions = ["Cache Service"] as String[]
        def itemTypes = [SimpleType.STRING] as OpenType[]
        def indexNames = ["Cache Service"] as String[]

        try {
            def pageType = new CompositeType("page", "Page size info", itemNamesAndDescriptions,
                itemNamesAndDescriptions, itemTypes)

            def pageTabularType = new TabularType("List of Cache Services", "List of Cache Services",
                pageType, indexNames)

            tabularDataSupport = new TabularDataSupport(pageTabularType)

            cacheServices.collect { it.class.name }.each { className ->
                tabularDataSupport.put(
                    new CompositeDataSupport(pageType, itemNamesAndDescriptions, [className] as Object[]))
            }
        } catch (exception) {
            LOG.error "An exception occurred building tabulardata for the registered cache services.", exception
        }

        tabularDataSupport
    }

    protected void bindCacheService(CacheService cacheService) {
        cacheServices.add(cacheService)
    }

    protected void unbindCacheService(CacheService cacheService) {
        cacheServices.remove(cacheService)
    }
}
