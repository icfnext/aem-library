package com.icfolson.aem.library.core.node.impl

import com.icfolson.aem.library.api.node.ComponentNode
import com.icfolson.aem.library.core.node.predicates.ComponentNodePropertyExistsPredicate
import com.google.common.base.Predicate
import com.icfolson.aem.library.core.specs.AemLibrarySpec
import spock.lang.Unroll

@Unroll
class DefaultComponentNodeSpec extends AemLibrarySpec {

    def setupSpec() {
        pageBuilder.content {
            citytechinc("CITYTECH, Inc.") {
                "jcr:content"(otherPagePath: "/content/ales/esb") {
                    nsfwImage(fileReference: "omg.png")
                    beer()
                    whiskey("sling:resourceType": "rye")
                    malort {
                        one("sling:resourceType": "won")
                        two("sling:resourceType": "tew")
                    }
                }
            }
            ales {
                esb("ESB") {
                    "jcr:content"(otherPagePath: "/content/citytechinc", externalPath: "http://www.reddit.com") {
                        image(fileReference: "/content/dam/image")
                        secondimage(fileReference: "/content/dam/image")
                        fullers("sling:resourceType": "bitter")
                        morland("sling:resourceType": "bitter")
                        greeneking("sling:resourceType": "bitter")
                    }
                    suds {
                        "jcr:content"(otherPagePath: "") {
                            container {
                                child1("jcr:title": "Zeus")
                                child2()
                            }
                        }
                        pint {
                            keg { "jcr:content" { container() } }
                            barrel {
                                "jcr:content" { container { child1() } }
                            }
                        }
                    }
                    bar {
                        "jcr:content" {
                            wood {
                                container {
                                    pine()
                                    spruce()
                                    maple()
                                }
                            }
                        }
                        tree { "jcr:content" { wood() } }
                    }
                    lace {
                        "jcr:content"() {
                            parent {
                                child1("sling:resourceType": "unknown")
                                child2("sling:resourceType": "unknown")
                                child3("sling:resourceType": "known")
                            }
                        }
                    }
                }
            }
            lagers {
                "jcr:content"(otherPagePath: "/content/citytechinc") {
                    dynamo("sling:resourceType": "us")
                    stiegl("sling:resourceType": "de")
                    spaten("sling:resourceType": "de")
                }
            }
            inheritance {
                "jcr:content"("jcr:title": "Inheritance") {
                    component("jcr:title": "Component", "number": 5, "boolean": false) {
                        image(fileReference: "/content/dam/image")
                        secondimage(fileReference: "/content/dam/image")
                        insidecomponent(fileReference: "/content/dam/image")
                    }
                }
                child {
                    "jcr:content" {
                        component() { insidecomponent() }
                        other()
                    }
                }
            }
        }

        nodeBuilder.content {
            dam("sling:Folder") {
                image("dam:Asset") {
                    "jcr:content"("jcr:data": "data") {
                        renditions("nt:folder") {
                            original("nt:file") {
                                "jcr:content"("nt:resource", "jcr:data": "data")
                            }
                        }
                    }
                }
            }
        }
    }

    def "get parent"() {
        setup:
        def node = getComponentNode(path)

        expect:
        node.parent.get().path == parentPath

        where:
        path                               | parentPath
        "/content/citytechinc/jcr:content" | "/content/citytechinc"
        "/content/citytechinc"             | "/content"
    }

    def "get parent returns null for root node"() {
        setup:
        def node = getComponentNode("/")

        expect:
        !node.parent.present
    }

    def "find ancestor with property"() {
        setup:
        def node = getComponentNode(path)
        def ancestorNodeOptional = node.findAncestorWithProperty("jcr:title")

        expect:
        ancestorNodeOptional.get().path == ancestorPath

        where:
        path                                               | ancestorPath
        "/content/inheritance/jcr:content"                 | "/content/inheritance/jcr:content"
        "/content/inheritance/child/jcr:content"           | "/content/inheritance/jcr:content"
        "/content/inheritance/child/jcr:content/component" | "/content/inheritance/jcr:content/component"
    }

    def "find ancestor returns absent"() {
        setup:
        def node = getComponentNode(path)
        def ancestorNodeOptional = node.findAncestorWithProperty("jcr:description")

        expect:
        !ancestorNodeOptional.present

        where:
        path << [
            "/content/inheritance/child/jcr:content",
            "/content/inheritance/child/jcr:content/component"
        ]
    }

    def "find ancestor with property value"() {
        setup:
        def node = getComponentNode("/content/inheritance/child/jcr:content/component")
        def ancestorNodeOptional = node.findAncestorWithPropertyValue(propertyName, propertyValue)

        expect:
        ancestorNodeOptional.get().path == "/content/inheritance/jcr:content/component"

        where:
        propertyName | propertyValue
        "jcr:title"  | "Component"
        "number"     | Long.valueOf(5)
        "boolean"    | false
    }

    def "find ancestor with property value returns absent"() {
        setup:
        def node = getComponentNode("/content/inheritance/child/jcr:content/component")
        def ancestorNodeOptional = node.findAncestorWithPropertyValue("jcr:title", "Komponent")

        expect:
        !ancestorNodeOptional.present
    }

    def "find descendants"() {
        setup:
        def node = getComponentNode("/content/citytechinc/jcr:content")
        def predicate = new ComponentNodePropertyExistsPredicate("sling:resourceType")

        expect:
        node.findDescendants(predicate).size() == 3
    }

    def "get as href inherited"() {
        setup:
        def node = getComponentNode(path)

        expect:
        node.getAsHrefInherited(propertyName).get() == href

        where:
        path                                 | propertyName    | href
        "/content/ales/esb/jcr:content"      | "otherPagePath" | "/content/citytechinc.html"
        "/content/ales/esb/suds/jcr:content" | "otherPagePath" | ""
        "/content/ales/esb/lace/jcr:content" | "otherPagePath" | "/content/citytechinc.html"
        "/content/ales/esb/jcr:content"      | "externalPath"  | "http://www.reddit.com"
        "/content/ales/esb/suds/jcr:content" | "externalPath"  | "http://www.reddit.com"
        "/content/ales/esb/lace/jcr:content" | "externalPath"  | "http://www.reddit.com"
    }

    def "get as href inherited returns absent where appropriate"() {
        setup:
        def node = getComponentNode(path)

        expect:
        !node.getAsHrefInherited(propertyName).present

        where:
        path                                 | propertyName
        "/content/ales/esb/jcr:content"      | "nonExistentPath"
        "/content/ales/esb/suds/jcr:content" | "nonExistentPath"
    }

    def "get as link inherited"() {
        setup:
        def node = getComponentNode(path)

        expect:
        node.getAsLinkInherited(propertyName).get().href == href

        where:
        path                                 | propertyName    | href
        "/content/ales/esb/jcr:content"      | "otherPagePath" | "/content/citytechinc.html"
        "/content/ales/esb/suds/jcr:content" | "otherPagePath" | ""
        "/content/ales/esb/lace/jcr:content" | "otherPagePath" | "/content/citytechinc.html"
        "/content/ales/esb/jcr:content"      | "externalPath"  | "http://www.reddit.com"
        "/content/ales/esb/suds/jcr:content" | "externalPath"  | "http://www.reddit.com"
        "/content/ales/esb/lace/jcr:content" | "externalPath"  | "http://www.reddit.com"
    }

    def "get as link inherited returns absent where appropriate"() {
        setup:
        def node = getComponentNode(path)

        expect:
        !node.getAsLinkInherited("nonExistentPath").present

        where:
        path                                 | propertyName
        "/content/ales/esb/jcr:content"      | "nonExistentPath"
        "/content/ales/esb/suds/jcr:content" | "nonExistentPath"
    }

    def "get as page inherited"() {
        setup:
        def node = getComponentNode("/content/ales/esb/lace/jcr:content")

        expect:
        node.getAsPageInherited("otherPagePath").get().path == "/content/citytechinc"
        !node.getAsPageInherited("nonExistentPagePath").present
    }

    def "get node inherited"() {
        setup:
        def node = getComponentNode(path)

        expect:
        node.getNodeInherited("child1").get().path == inheritedNodePath

        where:
        path                                                       | inheritedNodePath
        "/content/ales/esb/suds/pint/keg/jcr:content/container"    | "/content/ales/esb/suds/jcr:content/container/child1"
        "/content/ales/esb/suds/pint/barrel/jcr:content/container" | "/content/ales/esb/suds/pint/barrel/jcr:content/container/child1"
    }

    def "get node inherited is absent when ancestor not found"() {
        expect:
        !getComponentNode("/content/ales/esb/jcr:content").getNodeInherited("child1").present
    }

    def "get nodes inherited"() {
        setup:
        def node = getComponentNode(path)

        expect:
        node.getNodesInherited("container").size() == size

        where:
        path                                             | size
        "/content/ales/esb/suds/pint/jcr:content"        | 2
        "/content/ales/esb/suds/pint/keg/jcr:content"    | 0
        "/content/ales/esb/suds/pint/barrel/jcr:content" | 1
        "/content/ales/esb/bar/tree/jcr:content/wood"    | 3
    }

    def "get image source inherited optional"() {
        setup:
        def node = getComponentNode(path)

        expect:
        node.imageSourceInherited.present == isPresent

        where:
        path                                                               | isPresent
        "/content/ales/esb/jcr:content"                                    | true
        "/content/ales/esb/suds/jcr:content"                               | true
        "/content/ales/esb/suds/pint/jcr:content"                          | true
        "/content/inheritance/jcr:content"                                 | false
        "/content/inheritance/child/jcr:content"                           | false
        "/content/inheritance/jcr:content/component"                       | true
        "/content/inheritance/child/jcr:content/component"                 | true
        "/content/ales/esb/jcr:content/fullers"                            | false
        "/content/inheritance/jcr:content/component/insidecomponent"       | true
        "/content/inheritance/child/jcr:content/component/insidecomponent" | true
    }

    def "get image source inherited"() {
        setup:
        def node = getComponentNode(path)

        expect:
        node.imageSourceInherited.get() == imageSrc

        where:
        path                                                               | imageSrc
        "/content/ales/esb/jcr:content"                                    | "/content/ales/esb.img.png"
        "/content/ales/esb/suds/jcr:content"                               | "/content/ales/esb.img.png"
        "/content/ales/esb/suds/pint/jcr:content"                          | "/content/ales/esb.img.png"
        "/content/inheritance/jcr:content/component"                       | "/content/inheritance/jcr:content/component.img.png"
        "/content/inheritance/child/jcr:content/component"                 | "/content/inheritance/jcr:content/component.img.png"
        "/content/inheritance/jcr:content/component/insidecomponent"       | "/content/inheritance/jcr:content/component/insidecomponent.img.png"
        "/content/inheritance/child/jcr:content/component/insidecomponent" | "/content/inheritance/jcr:content/component/insidecomponent.img.png"
    }

    def "get named image source inherited"() {
        setup:
        def node = getComponentNode(path)

        expect:
        node.getImageSourceInherited(name).get() == imageSrc

        where:
        path                                 | name          | imageSrc
        "/content/ales/esb/suds/jcr:content" | "image"       | "/content/ales/esb.img.png"
        "/content/ales/esb/suds/jcr:content" | "secondimage" | "/content/ales/esb.img.secondimage.png"
    }

    def "get inherited"() {
        setup:
        def node = getComponentNode(path)

        expect:
        node.getInherited(propertyName, "") == propertyValue

        where:
        path                                                              | propertyName  | propertyValue
        "/content/ales/esb/suds/pint/barrel/jcr:content/container/child1" | "jcr:title"   | "Zeus"
        "/content/ales/esb/suds/pint/barrel/jcr:content/container/child1" | "nonExistent" | ""
        "/content/ales/esb/jcr:content/fullers"                           | "any"         | ""
    }

    def "get inherited optional"() {
        setup:
        def node = getComponentNode("/content/ales/esb/lace/jcr:content")

        expect:
        node.getInherited("otherPagePath", String).present
        !node.getInherited("nonExistentProperty", String).present
    }

    def "get component node at relative path"() {
        setup:
        def node = getComponentNode("/content/citytechinc/jcr:content")

        expect:
        node.getComponentNode("whiskey").present
        !node.getComponentNode("vodka").present
    }

    def "get component nodes"() {
        setup:
        def node = getComponentNode(path)

        expect:
        node.getComponentNodes().size() == size

        where:
        path                                    | size
        "/content/citytechinc/jcr:content"      | 4
        "/content/citytechinc/jcr:content/beer" | 0
    }

    def "get component nodes for predicate"() {
        setup:
        def node = getComponentNode("/content/citytechinc/jcr:content/malort")
        def predicate = new Predicate<ComponentNode>() {
            @Override
            boolean apply(ComponentNode input) {
                input.resource.resourceType == "tew"
            }
        }

        expect:
        node.getComponentNodes(predicate).size() == 1
    }

    def "get component nodes at relative path"() {
        setup:
        def node = getComponentNode("/content/citytechinc/jcr:content")

        expect:
        node.getComponentNodes(relativePath).size() == size

        where:
        relativePath | size
        "whiskey"    | 0
        "malort"     | 2
    }

    def "get component nodes at relative path for resource type"() {
        setup:
        def node = getComponentNode("/content/citytechinc/jcr:content")

        expect:
        node.getComponentNodes("malort", resourceType).size() == size

        where:
        resourceType   | size
        "non-existent" | 0
        "tew"          | 1
    }

    def "get component nodes at relative path for predicate"() {
        setup:
        def node = getComponentNode("/content/ales/esb/lace/jcr:content")
        def predicate = new Predicate<ComponentNode>() {
            @Override
            boolean apply(ComponentNode input) {
                input.resource.resourceType == "unknown"
            }
        }

        expect:
        node.getComponentNodes("parent", predicate).size() == 2
    }
}