package com.icfolson.aem.library.models.impl

import com.icfolson.aem.library.api.link.Link
import com.icfolson.aem.library.api.node.ComponentNode
import com.icfolson.aem.library.core.link.builders.factory.LinkBuilderFactory
import com.google.common.base.Function
import com.google.common.base.Optional
import com.icfolson.aem.library.models.annotations.LinkInject
import groovy.transform.TupleConstructor
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy
import org.apache.sling.models.spi.AcceptsNullName
import org.apache.sling.models.spi.DisposalCallbackRegistry
import org.apache.sling.models.spi.Injector
import org.apache.sling.models.spi.injectorspecific.AbstractInjectAnnotationProcessor2
import org.apache.sling.models.spi.injectorspecific.InjectAnnotationProcessor2
import org.apache.sling.models.spi.injectorspecific.InjectAnnotationProcessorFactory2
import org.osgi.framework.Constants

import java.lang.reflect.AnnotatedElement

@Component
@Service(Injector)
@Property(name = Constants.SERVICE_RANKING, intValue = 4000)
class LinkInjector extends AbstractTypedComponentNodeInjector<Link> implements Injector,
    InjectAnnotationProcessorFactory2, AcceptsNullName {

    @Override
    String getName() {
        LinkInject.NAME
    }

    @Override
    Object getValue(ComponentNode componentNode, String name, Class<Link> declaredType, AnnotatedElement element,
        DisposalCallbackRegistry callbackRegistry) {
        def injectAnnotation = element.getAnnotation(LinkInject)

        Optional<String> pathOptional

        String title = null

        if (injectAnnotation) {
            if (injectAnnotation.inherit()) {
                pathOptional = componentNode.getInherited(name, String)

                if (injectAnnotation.titleProperty()) {
                    title = componentNode.getInherited(injectAnnotation.titleProperty(), String).orNull()
                }
            } else {
                pathOptional = componentNode.get(name, String)

                if (injectAnnotation.titleProperty()) {
                    title = componentNode.get(injectAnnotation.titleProperty(), String).orNull()
                }
            }
        } else {
            pathOptional = componentNode.get(name, String)
        }

        pathOptional.transform(new Function<String, Link>() {
            @Override
            Link apply(String path) {
                LinkBuilderFactory.forPath(path).setTitle(title).build()
            }
        }).orNull()
    }

    @Override
    InjectAnnotationProcessor2 createAnnotationProcessor(Object adaptable, AnnotatedElement element) {
        // check if the element has the expected annotation
        def annotation = element.getAnnotation(LinkInject)

        annotation ? new LinkAnnotationProcessor(annotation) : null
    }

    @TupleConstructor
    private static class LinkAnnotationProcessor extends AbstractInjectAnnotationProcessor2 {

        LinkInject annotation

        @Override
        InjectionStrategy getInjectionStrategy() {
            annotation.injectionStrategy()
        }
    }
}
