package com.icfolson.aem.library.models.impl

import com.icfolson.aem.library.api.node.ComponentNode
import com.icfolson.aem.library.models.annotations.InheritInject
import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy
import org.apache.sling.models.spi.DisposalCallbackRegistry
import org.apache.sling.models.spi.Injector
import org.apache.sling.models.spi.injectorspecific.AbstractInjectAnnotationProcessor2
import org.apache.sling.models.spi.injectorspecific.InjectAnnotationProcessor2
import org.apache.sling.models.spi.injectorspecific.InjectAnnotationProcessorFactory2
import org.osgi.framework.Constants

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Type

@Component
@Service(Injector)
@Property(name = Constants.SERVICE_RANKING, intValue = 4000)
@Slf4j("LOG")
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
            if (declaredType instanceof Class && declaredType.enum) {
                def enumString = componentNode.getInherited(name, String)

                value = enumString.present ? declaredType[enumString.get()] : null
            }

            try {
                value = componentNode.getInherited(name, declaredType as Class).orNull()
            } catch (Exception e) {
                LOG.debug("Error getting object inherited", e)
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
