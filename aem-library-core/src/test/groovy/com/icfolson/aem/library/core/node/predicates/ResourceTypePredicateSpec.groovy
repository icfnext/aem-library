package com.icfolson.aem.library.core.node.predicates

import com.icfolson.aem.library.core.specs.AemLibrarySpec
import spock.lang.Unroll

@Unroll
class ResourceTypePredicateSpec extends AemLibrarySpec {

    def setupSpec() {
        nodeBuilder.content {
            citytechinc("sling:resourceType": "page")
        }
    }

    def "resource with matching resource type is included"() {
        setup:
        def resource = resourceResolver.getResource("/content/citytechinc")
        def predicate = new ResourceTypePredicate(resourceType)

        expect:
        predicate.apply(resource) == result

        where:
        resourceType | result
        "page"       | true
        "node"       | false
    }


}
