package com.icfolson.aem.library.models.impl

import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.resource.Resource

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

trait ModelTrait {

    SlingHttpServletRequest getRequest(Object adaptable) {
        def request = null

        if (adaptable instanceof SlingHttpServletRequest) {
            request = adaptable as SlingHttpServletRequest
        }

        request
    }

    Resource getResource(Object adaptable) {
        def resource = null

        if (adaptable instanceof Resource) {
            resource = adaptable as Resource
        } else if (adaptable instanceof SlingHttpServletRequest) {
            resource = (adaptable as SlingHttpServletRequest).resource
        }

        resource
    }

    boolean isDeclaredTypeCollection(Type declaredType) {
        def result = false

        if (declaredType instanceof ParameterizedType) {
            def parameterizedType = declaredType as ParameterizedType
            def collectionType = parameterizedType.rawType as Class

            result = Collection.isAssignableFrom(collectionType)
        }

        result
    }

    Class getDeclaredClassForDeclaredType(Type declaredType) {
        def clazz

        if (isDeclaredTypeCollection(declaredType)) {
            clazz = (declaredType as ParameterizedType).actualTypeArguments[0] as Class
        } else {
            clazz = declaredType as Class
        }

        clazz
    }
}
