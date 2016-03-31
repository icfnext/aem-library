package com.icfolson.aem.library.models.impl

import com.icfolson.aem.library.api.node.ComponentNode

import org.apache.sling.models.spi.DisposalCallbackRegistry
import org.apache.sling.models.spi.Injector

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Type

import javax.inject.Named

abstract class AbstractComponentNodeInjector implements Injector, ModelTrait {

	abstract Object getValue(ComponentNode componentNode, String name, Type declaredType, AnnotatedElement element,
        DisposalCallbackRegistry callbackRegistry)

	@Override
	Object getValue(Object adaptable, String name, Type declaredType, AnnotatedElement element,
			DisposalCallbackRegistry callbackRegistry) {
		def value = null

		def componentNode = getResource(adaptable)?.adaptTo(ComponentNode)

		if (componentNode) {
			value = getValue(componentNode, element.getAnnotation(Named)?.value() ?: name, declaredType, element,
                callbackRegistry)
		}

		value
	}
}
