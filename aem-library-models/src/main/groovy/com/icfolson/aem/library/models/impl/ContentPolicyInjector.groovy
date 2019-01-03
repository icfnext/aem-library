package com.icfolson.aem.library.models.impl

import com.day.cq.wcm.api.policies.ContentPolicy
import com.day.cq.wcm.api.policies.ContentPolicyManager
import groovy.util.logging.Slf4j
import org.apache.sling.models.spi.DisposalCallbackRegistry
import org.apache.sling.models.spi.Injector
import org.osgi.service.component.annotations.Component

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Type

/**
 * Injects the content policy for the current resource.
 */
@Component(service = Injector, property = [
    "service.ranking:Integer=9999"
])
@Slf4j("LOG")
class ContentPolicyInjector implements Injector, ModelTrait {

    @Override
    String getName() {
        "content-policy"
    }

    @Override
    Object getValue(Object adaptable, String name, Type type, AnnotatedElement element,
        DisposalCallbackRegistry registry) {
        def value = null

        if (type instanceof Class) {
            def clazz = type as Class

            if (clazz == ContentPolicy) {
                value = getContentPolicy(adaptable)
            }
        }

        value
    }

    private ContentPolicy getContentPolicy(Object adaptable) {
        def resource = getResource(adaptable)

        def contentPolicy = null

        if (resource) {
            def contentPolicyManager = resource.resourceResolver.adaptTo(ContentPolicyManager)

            contentPolicy = contentPolicyManager.getPolicy(resource)
        }

        contentPolicy
    }
}
