package com.icfolson.aem.library.core.services.cache

import com.google.common.cache.Cache
import com.google.common.cache.CacheStats
import org.slf4j.Logger

import java.lang.reflect.Field

import static com.google.common.base.Preconditions.checkNotNull

trait CacheServiceTrait implements CacheService {

    @Override
    boolean clearAllCaches() {
        boolean cleared = false

        collectFields().each { field ->
            try {
                getCache(field).invalidateAll()
                cleared = true
            } catch (Exception e) {
                logger.error("An error has occurred while attempting to invalidate cache values for " +
                    "${field.name} in the class ${this.class.name}.", e)
            }
        }

        cleared
    }

    @Override
    boolean clearSpecificCache(String cacheVariableName) {
        checkNotNull(cacheVariableName, "cache name must not be null")

        boolean cleared = false

        collectFields(cacheVariableName).each { field ->
            try {
                getCache(field).invalidateAll()
                cleared = true
            } catch (Exception e) {
                logger.error("An error has occurred while attempting to invalidate cache values for " +
                    "${field.name} in the class ${this.class.name}.", e)
            }
        }

        cleared
    }

    @Override
    Long getCacheSize(String cacheVariableName) {
        checkNotNull(cacheVariableName, "cache name must not be null")

        def cacheSize = 0L

        collectFields(cacheVariableName).each { field ->
            try {
                cacheSize = getCache(field).size()
            } catch (Exception e) {
                logger.error("An error has occurred while attempting retrieve cache size for ${field.name} in " +
                    "the class ${this.class.name}.", e)
            }
        }

        cacheSize
    }

    @Override
    CacheStats getCacheStats(String cacheVariableName) {
        checkNotNull(cacheVariableName, "cache name must not be null")

        def cacheStats = null

        collectFields(cacheVariableName).each { field ->
            try {
                cacheStats = getCache(field).stats()
            } catch (Exception e) {
                logger.error("An error has occurred while attempting retrieve cache statistics for ${field.name} " +
                    "in the class ${this.class.name}.", e)
            }
        }

        cacheStats
    }

    @Override
    List<String> listCaches() {
        collectFields()*.name
    }

    abstract Logger getLogger()

    List<Field> collectFields() {
        collectFields(this.class)
    }

    List<Field> collectFields(String cacheVariableName) {
        collectFields(this.class, cacheVariableName)
    }

    List<Field> collectFields(Class clazz) {
        def fields = []

        fields.addAll(clazz.declaredFields.findAll { isCache(it) })

        if (clazz.superclass) {
            fields.addAll(collectFields(clazz.superclass))
        }

        fields
    }

    List<Field> collectFields(Class clazz, String cacheVariableName) {
        def fields = []

        if (clazz) {
            fields.addAll(clazz.declaredFields.findAll { isNamedCache(it, cacheVariableName) })

            if (clazz.superclass) {
                fields.addAll(collectFields(clazz.superclass, cacheVariableName))
            }
        }

        fields
    }

    boolean isNamedCache(Field field, String cacheVariableName) {
        isCache(field) && cacheVariableName == field.name
    }

    boolean isCache(Field field) {
        isCacheType(field) || isAssignableFromCache(field)
    }

    Cache getCache(Field field) {
        field.accessible = true

        field.get(this) as Cache
    }

    private boolean isCacheType(Field field) {
        field.type == Cache
    }

    private boolean isAssignableFromCache(Field field) {
        Cache.isAssignableFrom(field.type)
    }
}
