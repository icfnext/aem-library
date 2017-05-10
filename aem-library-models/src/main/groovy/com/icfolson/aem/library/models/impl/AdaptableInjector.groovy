package com.icfolson.aem.library.models.impl

import org.apache.sling.models.spi.AcceptsNullName
import org.apache.sling.models.spi.DisposalCallbackRegistry
import org.apache.sling.models.spi.Injector
import org.osgi.service.component.annotations.Component

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Type

/**
 * Injector for objects that are adaptable from the Sling resource resolver.
 */
@Component(service = Injector, property = [
    "service.ranking:Integer=-9999"
])
class AdaptableInjector implements Injector, ModelTrait, AcceptsNullName {

    @Override
    String getName() {
        "adaptable"
    }

    @Override
    Object getValue(Object adaptable, String name, Type type, AnnotatedElement element,
        DisposalCallbackRegistry registry) {
        def value = null

        if (type instanceof Class) {
            def clazz = type as Class

            def resourceResolver = getResource(adaptable)?.resourceResolver

            if (resourceResolver) {
                value = resourceResolver.adaptTo(clazz)
            }
        }

        value
    }
}
