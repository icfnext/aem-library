package com.icfolson.aem.library.models.impl

import com.icfolson.aem.library.models.specs.AemLibraryModelSpec
import spock.lang.Unroll

@Unroll
class AemLibraryModelComponentSpec extends AemLibraryModelSpec {

    def setupSpec() {
        pageBuilder.content {
            citytechinc {
                "jcr:content" {
                    component("jcr:title": "Testing Component")
                }
            }
        }
    }

    def "get title from component"() {
        setup:
        def component = getResource("/content/citytechinc/jcr:content/component").adaptTo(AemLibraryModelComponent)

        expect:
        component.title == "Testing Component"
    }
}
