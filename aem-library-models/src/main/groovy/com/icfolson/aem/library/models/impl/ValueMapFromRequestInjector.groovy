package com.icfolson.aem.library.models.impl

import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service
import org.apache.sling.models.spi.DisposalCallbackRegistry
import org.apache.sling.models.spi.Injector
import org.osgi.framework.Constants

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Type

@Component
@Service(Injector)
@Property(name = Constants.SERVICE_RANKING, intValue = 2500)
class ValueMapFromRequestInjector implements Injector, ModelTrait {

    @Override
    String getName() {
        "valuemap-request"
    }

    @Override
    Object getValue(Object adaptable, String name, Type type, AnnotatedElement element,
        DisposalCallbackRegistry callbackRegistry) {
        def value = null

        def request = getRequest(adaptable)

        if (request?.resource) {
            def map = request.resource.valueMap

            if (map && type instanceof Class<?>) {
                value = map.get(name, (Class<?>) type)
            }
        }

        value
    }
}
