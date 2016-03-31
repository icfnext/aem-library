package com.icfolson.aem.library.core.node.predicates

import com.icfolson.aem.library.core.specs.AemLibrarySpec

class ComponentNodeResourceTypePredicateSpec extends AemLibrarySpec {

    def setupSpec() {
        nodeBuilder.sabbath("sling:resourceType": "black") {
            paranoid()
        }
    }

    def "exception thrown when resource type is null"() {
        when:
        new ComponentNodeResourceTypePredicate(null)

        then:
        thrown(NullPointerException)
    }

    def "node with matching resource type is included"() {
        setup:
        def node = getComponentNode("/sabbath")
        def predicate = new ComponentNodeResourceTypePredicate("black")

        expect:
        predicate.apply(node)
    }

    def "node with non-matching resource type is not included"() {
        setup:
        def node = getComponentNode("/sabbath")
        def predicate = new ComponentNodeResourceTypePredicate("purple")

        expect:
        !predicate.apply(node)
    }

    def "node with no resource type is not included"() {
        setup:
        def node = getComponentNode("/sabbath/paranoid")
        def predicate = new ComponentNodeResourceTypePredicate("purple")

        expect:
        !predicate.apply(node)
    }
}
