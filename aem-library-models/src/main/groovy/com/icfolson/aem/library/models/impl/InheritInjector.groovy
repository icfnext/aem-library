package com.icfolson.aem.library.models.impl

import com.icfolson.aem.library.api.node.ComponentNode
import com.icfolson.aem.library.models.annotations.InheritInject
import groovy.transform.TupleConstructor
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy
import org.apache.sling.models.spi.DisposalCallbackRegistry
import org.apache.sling.models.spi.Injector
import org.apache.sling.models.spi.injectorspecific.AbstractInjectAnnotationProcessor2
import org.apache.sling.models.spi.injectorspecific.InjectAnnotationProcessor2
import org.apache.sling.models.spi.injectorspecific.InjectAnnotationProcessorFactory2
import org.osgi.service.component.annotations.Component

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@Component(service = Injector, property = [
    "service.ranking:Integer=4000"
])
class InheritInjector extends AbstractComponentNodeInjector implements InjectAnnotationProcessorFactory2 {

    @Override
    String getName() {
        InheritInject.NAME
    }

    @Override
    Object getValue(ComponentNode componentNode, String name, Type declaredType, AnnotatedElement element,
        DisposalCallbackRegistry callbackRegistry) {
        def value = null

        if (element.getAnnotation(InheritInject)) {
            if (isParameterizedListType(declaredType)) {
                def typeClass = getActualType((ParameterizedType) declaredType)

                value = componentNode.getComponentNodesInherited(name).collect { node ->
                    node.resource.adaptTo(typeClass)
                }
            } else if (declaredType instanceof Class && declaredType.enum) {
                def enumString = componentNode.getInherited(name, String)

                value = enumString.present ? declaredType[enumString.get()] : null
            } else {
                value = componentNode.getInherited(name, declaredType as Class).orNull()
            }
        }

        value
    }

    @Override
    InjectAnnotationProcessor2 createAnnotationProcessor(Object adaptable, AnnotatedElement element) {
        def annotation = element.getAnnotation(InheritInject)

        annotation ? new InheritAnnotationProcessor(annotation) : null
    }

    @TupleConstructor
    private static class InheritAnnotationProcessor extends AbstractInjectAnnotationProcessor2 {

        InheritInject annotation

        @Override
        InjectionStrategy getInjectionStrategy() {
            annotation.injectionStrategy()
        }
    }
}
