package com.icfolson.aem.library.core.node

import com.icfolson.aem.library.api.node.ComponentNode
import com.icfolson.aem.library.core.specs.AemLibrarySpec

class DelegateComponentNodeSpec extends AemLibrarySpec {

    class TestNode extends DelegateComponentNode {

        TestNode(ComponentNode componentNode) {
            super(componentNode)
        }

        def getTitle() {
            get("jcr:title", "")
        }
    }

    def setupSpec() {
        pageBuilder.content {
            home("Home")
        }
    }

    def "delegate"() {
        setup:
        def node = getComponentNode("/content/home/jcr:content")
        def delegatingNode = new TestNode(node)

        expect:
        delegatingNode.title == "Home"
    }
}
