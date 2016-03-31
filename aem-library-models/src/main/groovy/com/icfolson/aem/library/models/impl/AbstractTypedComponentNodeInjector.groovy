package com.icfolson.aem.library.models.impl

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

import javax.inject.Named

import org.apache.sling.models.spi.DisposalCallbackRegistry
import org.apache.sling.models.spi.Injector

import com.icfolson.aem.library.api.node.ComponentNode

abstract class AbstractTypedComponentNodeInjector<T> implements Injector, ModelTrait {

	abstract Object getValue(ComponentNode componentNode, String name, Class<T> declaredType, AnnotatedElement element,
	DisposalCallbackRegistry callbackRegistry)

	@Override
	final Object getValue(Object adaptable, String name, Type declaredType, AnnotatedElement element,
			DisposalCallbackRegistry callbackRegistry) {
		def clazz = (getClass().genericSuperclass as ParameterizedType).actualTypeArguments[0]

		def value = null

		if (declaredType == clazz) {
			def componentNode = getResource(adaptable)?.adaptTo(ComponentNode)

			if (componentNode) {
				value = getValue(componentNode, element.getAnnotation(Named)?.value() ?: name, declaredType, element, callbackRegistry)
			}
		}

		value
	}
}
