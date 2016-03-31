package com.icfolson.aem.library.models.impl

import com.icfolson.aem.library.models.annotations.InheritInject
import com.icfolson.aem.library.models.specs.AemLibraryModelSpec
import org.apache.sling.api.resource.Resource
import org.apache.sling.models.annotations.DefaultInjectionStrategy
import org.apache.sling.models.annotations.Model

import javax.inject.Inject

class InheritInjectorSpec extends AemLibraryModelSpec {

    @Model(adaptables = Resource, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    static class InheritModel {

        @InheritInject
        String title

        @InheritInject
        String[] items

        @Inject
        String text
    }

    def setupSpec() {
        pageBuilder.content {
            citytechinc {
                "jcr:content" { component("title": "Testing Component", "items": ["item1", "item2"],
                    "text": "Not Inherited") }
                page1 {
                    "jcr:content" {
                        component()
                    }
                }
            }
        }
    }

    def "Test inherit"() {
        setup:
        def resource = resourceResolver.resolve("/content/citytechinc/page1/jcr:content/component")
        def component = resource.adaptTo(InheritModel)

        expect:
        component.title == "Testing Component"
        component.items.length == 2
        component.text == null
    }
}