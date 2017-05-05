package com.icfolson.aem.library.models.impl

import com.google.common.base.Optional
import com.icfolson.aem.library.api.node.ComponentNode
import org.apache.sling.models.spi.DisposalCallbackRegistry
import org.apache.sling.models.spi.Injector
import org.osgi.service.component.annotations.Component

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Type

@Component(service = Injector, property = [
    "service.ranking:Integer=4000"
])
class EnumInjector extends AbstractComponentNodeInjector {

    @Override
    String getName() {
        "enum"
    }

    @Override
    Object getValue(ComponentNode componentNode, String name, Type declaredType, AnnotatedElement element,
        DisposalCallbackRegistry callbackRegistry) {
        def value = null

        if (declaredType instanceof Class && declaredType.enum) {
            Optional<String> enumString = componentNode.get(name, String)

            if (enumString.present) {
                value = declaredType[enumString.get()]
            }
        }

        value
    }
}
