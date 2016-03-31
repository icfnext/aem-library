package com.icfolson.aem.library.models.impl

import com.icfolson.aem.library.models.specs.AemLibraryModelSpec
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.models.annotations.Model
import org.apache.sling.models.annotations.Optional

import javax.inject.Inject

class EnumInjectorSpec extends AemLibraryModelSpec {

    static enum Beer {
        ALE, LAGER
    }

    @Model(adaptables = SlingHttpServletRequest)
    static class EnumInjectorModel {

        @Inject
        @Optional
        Beer beer
    }

    def setupSpec() {
        pageBuilder.content {
            citytechinc {
                "jcr:content" {
                    component(beer: "LAGER")
                }
            }
        }
    }

    def "enum is injected when component property value is valid"() {
        setup:
        def request = requestBuilder.build {
            path = "/content/citytechinc/jcr:content/component"
        }

        def model = request.adaptTo(EnumInjectorModel)

        expect:
        model.beer == Beer.LAGER
    }

    def "enum is null when component property does not exist"() {
        setup:
        def request = requestBuilder.build {
            path = "/content/citytechinc/jcr:content"
        }

        def model = request.adaptTo(EnumInjectorModel)

        expect:
        !model.beer
    }
}
