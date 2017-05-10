package com.icfolson.aem.library.models.impl

import com.icfolson.aem.library.api.node.ComponentNode
import com.icfolson.aem.library.models.annotations.ReferenceInject
import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j
import org.apache.sling.api.resource.Resource
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy
import org.apache.sling.models.spi.DisposalCallbackRegistry
import org.apache.sling.models.spi.Injector
import org.apache.sling.models.spi.injectorspecific.AbstractInjectAnnotationProcessor2
import org.apache.sling.models.spi.injectorspecific.InjectAnnotationProcessor2
import org.apache.sling.models.spi.injectorspecific.InjectAnnotationProcessorFactory2
import org.osgi.service.component.annotations.Component

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Type

@Component(service = Injector, property = [
    "service.ranking:Integer=4000"
])
@Slf4j("LOG")
class ReferenceInjector extends AbstractComponentNodeInjector implements InjectAnnotationProcessorFactory2, ModelTrait {

    @Override
    Object getValue(ComponentNode componentNode, String name, Type declaredType, AnnotatedElement element,
        DisposalCallbackRegistry callbackRegistry) {
        def annotation = element.getAnnotation(ReferenceInject)
        def declaredClass = getDeclaredClassForDeclaredType(declaredType)

        if (annotation) {
            def references = annotation.inherit() ? componentNode.getAsListInherited(name,
                String) : componentNode.getAsList(name, String)

            def resourceResolver = componentNode.resource.resourceResolver

            def referencedObjects = references.collect {
                def referencedResource = it.startsWith("/") ? resourceResolver.getResource(
                    it) : resourceResolver.getResource(componentNode.resource, it)

                if (!referencedResource) {
                    LOG.warn("Reference {} did not resolve to an accessible Resource", it)
                }

                if (referencedResource && declaredClass != Resource) {
                    def adaptedObject = referencedResource.adaptTo(declaredClass)

                    if (!adaptedObject) {
                        LOG.warn("Resource at {} could not be adapted to an instance of {}", referencedResource.path,
                            declaredClass.name)
                    }

                    return adaptedObject
                }

                return referencedResource
            }

            if (referencedObjects) {
                if (!isDeclaredTypeCollection(declaredType)) {
                    return referencedObjects[0]
                }

                return referencedObjects
            }
        }

        null
    }

    @Override
    InjectAnnotationProcessor2 createAnnotationProcessor(Object adaptable, AnnotatedElement element) {
        def annotation = element.getAnnotation(ReferenceInject)

        annotation ? new ReferenceAnnotationProcessor(annotation) : null
    }

    @Override
    String getName() {
        ReferenceInject.NAME
    }

    @TupleConstructor
    private static class ReferenceAnnotationProcessor extends AbstractInjectAnnotationProcessor2 {

        ReferenceInject annotation

        @Override
        InjectionStrategy getInjectionStrategy() {
            annotation.injectionStrategy()
        }
    }
}
