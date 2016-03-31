package com.icfolson.aem.library.core.node.predicates

import com.icfolson.aem.library.api.node.ComponentNode
import com.google.common.base.Optional
import com.icfolson.aem.library.core.node.impl.DefaultComponentNode
import com.icfolson.aem.library.core.specs.AemLibrarySpec
import org.apache.sling.api.resource.NonExistingResource

import javax.jcr.Node
import javax.jcr.RepositoryException

class ComponentNodePropertyExistsPredicateSpec extends AemLibrarySpec {

    def setupSpec() {
        nodeBuilder.content {
            citytechinc("jcr:title": "CITYTECH, Inc.")
        }
    }

    def "node where property exists is included"() {
        setup:
        def node = getComponentNode("/content/citytechinc")
        def predicate = new ComponentNodePropertyExistsPredicate("jcr:title")

        expect:
        predicate.apply(node)
    }

    def "node where property does not exist is not included"() {
        setup:
        def node = getComponentNode("/content/citytechinc")
        def predicate = new ComponentNodePropertyExistsPredicate("jcr:description")

        expect:
        !predicate.apply(node)
    }

    def "node for non-existing resource is not included"() {
        setup:
        def resource = new NonExistingResource(resourceResolver, "/content/non-existing")
        def node = new DefaultComponentNode(resource)
        def predicate = new ComponentNodePropertyExistsPredicate("propertyName")

        expect:
        !predicate.apply(node)
    }

    def "node that throws exception is not included"() {
        setup:
        def node = Mock(Node) {
            hasProperty(_) >> {
                throw new RepositoryException()
            }
        }

        def componentNode = Mock(ComponentNode) {
            getNode() >> Optional.of(node)
        }

        def predicate = new ComponentNodePropertyExistsPredicate("propertyName")

        expect:
        !predicate.apply(componentNode)
    }
}
