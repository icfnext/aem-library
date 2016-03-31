package com.icfolson.aem.library.models.impl

import com.icfolson.aem.library.api.node.BasicNode
import com.icfolson.aem.library.api.node.ComponentNode
import com.icfolson.aem.library.api.page.PageDecorator
import com.icfolson.aem.library.api.page.PageManagerDecorator
import com.icfolson.aem.library.core.specs.AemLibrarySpec
import org.apache.sling.api.resource.Resource
import org.apache.sling.api.resource.ResourceResolver
import org.apache.sling.api.resource.ValueMap
import spock.lang.Shared
import spock.lang.Unroll

import javax.jcr.Node
import javax.jcr.Session

@Unroll
class ComponentInjectorSpec extends AemLibrarySpec {

    @Shared
    ComponentInjector injector = new ComponentInjector()

    def setupSpec() {
        pageBuilder.content {
            citytechinc {
                "jcr:content" {
                    component("jcr:title": "Testing Component")
                }
				page1 {
                    "jcr:content" {
                        component()
                    }
                }
            }
        }
    }

    def "get value from resource for valid type returns non-null value"() {
        setup:
        def resource = getResource("/content/citytechinc/jcr:content/component")
        def value = injector.getValue(resource, null, type, null, null)

        expect:
        value

        where:
        type << [Resource, ResourceResolver, ValueMap, Node, Session, BasicNode, ComponentNode, PageDecorator, PageManagerDecorator]
    }

    def "get value from resource for invalid type returns null value"() {
        setup:
        def resource = getResource("/content/citytechinc/jcr:content/component")
        def value = injector.getValue(resource, null, String, null, null)

        expect:
        !value
    }
}
