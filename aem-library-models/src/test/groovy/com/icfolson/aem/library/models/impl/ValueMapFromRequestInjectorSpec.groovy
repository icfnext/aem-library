package com.icfolson.aem.library.models.impl

import com.icfolson.aem.library.models.specs.AemLibraryModelSpec
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.models.annotations.Model
import org.apache.sling.models.annotations.Optional

import javax.inject.Inject

class ValueMapFromRequestInjectorSpec extends AemLibraryModelSpec {

    @Model(adaptables = SlingHttpServletRequest)
    static class ValueMapInjectorModel {

        @Inject
        String name

        @Inject
        @Optional
        String title
    }

    def setupSpec() {
        pageBuilder.content {
            citytechinc {
                "jcr:content" {
                    component(name: "AEM Library")
                }
            }
        }

        slingContext.registerInjectActivateService(new ValueMapFromRequestInjector())
    }

    def "inject values for component"() {
        setup:
        def request = requestBuilder.build {
            path = "/content/citytechinc/jcr:content/component"
        }

        def model = request.adaptTo(ValueMapInjectorModel)

        expect:
        model.name == "AEM Library"

        and:
        !model.title
    }
}
