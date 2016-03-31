package com.icfolson.aem.library.core.services.cache

import com.google.common.cache.Cache
import com.google.common.cache.CacheStats
import com.google.common.cache.LoadingCache
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class CacheServiceSpec extends Specification {

    def "clear all caches"() {
        setup:
        def cacheService = testCacheService
        def johnny = Mock(Cache)
        def june = Mock(LoadingCache)

        cacheService.johnny = johnny
        cacheService.june = june

        when:
        cacheService.clearAllCaches()

        then:
        1 * johnny.invalidateAll()
        1 * june.invalidateAll()

        where:
        testCacheService << [new TestCacheServiceTrait(), new TestCacheService()]
    }

    def "clear specific cache"() {
        setup:
        def cacheService = testCacheService
        def johnny = Mock(Cache)
        def june = Mock(LoadingCache)

        cacheService.johnny = johnny
        cacheService.june = june

        when:
        cacheService.clearSpecificCache("june")

        then:
        0 * johnny.invalidateAll()
        1 * june.invalidateAll()

        where:
        testCacheService << [new TestCacheServiceTrait(), new TestCacheService()]
    }

    def "get cache size"() {
        setup:
        def cacheService = testCacheService
        def johnny = Mock(Cache) {
            size() >> Long.MAX_VALUE
        }

        cacheService.johnny = johnny

        expect:
        cacheService.getCacheSize("johnny") == Long.MAX_VALUE

        where:
        testCacheService << [new TestCacheServiceTrait(), new TestCacheService()]
    }

    def "get cache stats"() {
        setup:
        def cacheService = testCacheService
        def johnny = Mock(Cache) {
            stats() >> new CacheStats(Long.MAX_VALUE, 0, 0, 0, 0, 0)
        }

        cacheService.johnny = johnny

        expect:
        cacheService.getCacheStats("johnny").hitCount() == Long.MAX_VALUE

        where:
        testCacheService << [new TestCacheServiceTrait(), new TestCacheService()]
    }

    def "list caches"() {
        expect:
        cacheService.listCaches() == ["johnny", "june"]

        where:
        cacheService << [new TestCacheServiceTrait(), new TestCacheService()]
    }
}