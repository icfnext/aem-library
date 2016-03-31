package com.icfolson.aem.library.models.impl

import com.icfolson.aem.library.models.specs.AemLibraryModelSpec
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.resource.Resource
import org.apache.sling.models.annotations.Model

import javax.inject.Inject

class ModelListInjectorSpec extends AemLibraryModelSpec {

    @Model(adaptables = Resource)
    static class ValueMapInjectorModel {

        @Inject
        String name
    }

    @Model(adaptables = SlingHttpServletRequest)
    static class ListModel {

        @Inject
        List<ValueMapInjectorModel> rush
    }

    def setupSpec() {
        pageBuilder.content {
            citytechinc {
                "jcr:content" {
                    component {
                        rush {
                            drums(name: "Neil")
                            bass(name: "Geddy")
                            guitar(name: "Alex")
                        }
                    }
                }
            }
        }
    }

    def "inject list of models"() {
        setup:
        def request = requestBuilder.build {
            path = "/content/citytechinc/jcr:content/component"
        }

        def model = request.adaptTo(ListModel)

        expect:
        model.rush.size() == 3
        model.rush*.name == ["Neil", "Geddy", "Alex"]
    }
}
