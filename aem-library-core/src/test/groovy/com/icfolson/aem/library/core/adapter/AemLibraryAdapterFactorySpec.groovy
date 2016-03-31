package com.icfolson.aem.library.core.adapter

import com.icfolson.aem.library.api.node.BasicNode
import com.icfolson.aem.library.api.node.ComponentNode
import com.icfolson.aem.library.api.page.PageDecorator
import com.icfolson.aem.library.api.page.PageManagerDecorator
import com.icfolson.aem.library.core.specs.AemLibrarySpec
import spock.lang.Shared
import spock.lang.Unroll

@Unroll
class AemLibraryAdapterFactorySpec extends AemLibrarySpec {

    @Shared adapterFactory = new AemLibraryAdapterFactory()

    def "get resource adapter for node decorators returns non-null"() {
        setup:
        def resource = resourceResolver.getResource("/")

        expect:
        adapterFactory.getAdapter(resource, type)

        where:
        type << [ComponentNode, BasicNode]
    }

    def "resource adapt to page"() {
        setup:
        pageBuilder.content {
            home()
        }

        def resource = resourceResolver.getResource("/content/home")

        expect:
        adapterFactory.getAdapter(resource, PageDecorator)
    }

    def "resource adapt to page returns null for non-page node"() {
        setup:
        def resource = resourceResolver.getResource("/")

        expect:
        !adapterFactory.getAdapter(resource, PageDecorator)
    }

    def "get resource adapter for invalid type returns null"() {
        setup:
        def resource = resourceResolver.getResource("/")

        expect:
        !adapterFactory.getAdapter(resource, String)
    }

    def "get resource resolver adapter for valid type returns non-null"() {
        expect:
        adapterFactory.getAdapter(resourceResolver, PageManagerDecorator)
    }

    def "get resource resolver adapter for invalid type returns null"() {
        expect:
        !adapterFactory.getAdapter(resourceResolver, String)
    }

    def "get invalid adapter returns null"() {
        expect:
        !adapterFactory.getAdapter("", String)
    }
}
