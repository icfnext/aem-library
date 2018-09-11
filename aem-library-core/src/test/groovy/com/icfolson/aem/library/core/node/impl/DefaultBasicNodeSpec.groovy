package com.icfolson.aem.library.core.node.impl

import com.day.cq.dam.api.Asset
import com.google.common.base.Predicates
import com.icfolson.aem.library.api.node.ComponentNode
import com.icfolson.aem.library.core.node.predicates.PropertyNamePredicate
import com.icfolson.aem.library.core.specs.AemLibrarySpec
import org.apache.sling.api.resource.NonExistingResource
import spock.lang.IgnoreRest
import spock.lang.Unroll

@Unroll
class DefaultBasicNodeSpec extends AemLibrarySpec {

    def setupSpec() {
        pageBuilder.content {
            citytechinc("CITYTECH, Inc.") {
                "jcr:content"(otherPagePath: "/content/ales/esb", nonExistentPagePath: "/content/home",
                    externalPath: "http://www.reddit.com", multiValue: ["one", "two"],
                    fileReference: "/content/dam/image") {
                    image(fileReference: "/content/dam/image")
                    secondimage(fileReference: "/content/dam/image")
                    thirdimage()
                    nsfwImage(fileReference: "omg.png")
                    imageWithRenditions(fileReference: "/content/dam/image-renditions")
                    beer(label: "orval", abv: "9.0", oz: "12") {
                        image(fileReference: "/content/dam/image")
                        secondimage(fileReference: "/content/dam/image")
                    }
                    whiskey("sling:resourceType": "rye")
                    malort {
                        one("sling:resourceType": "won")
                        two("sling:resourceType": "tew")
                    }
                    bourbon("sling:resourceType": "yummy", fileReference: "/content/dam/image")
                }
            }
            ales {
                esb("ESB") {
                    "jcr:content"(otherPagePath: "/content/citytechinc") {
                        fullers("sling:resourceType": "bitter")
                        morland("sling:resourceType": "bitter")
                        greeneking("sling:resourceType": "bitter") {
                            image(fileReference: "/content/dam/image")
                        }
                    }
                }
            }
            lagers {
                "jcr:content"(otherPagePath: "/content/citytechinc") {
                    dynamo("sling:resourceType": "us", related: "/content/lagers/jcr:content/spaten")
                    stiegl("sling:resourceType": "de")
                    spaten("sling:resourceType": "de")
                }
            }
        }

        nodeBuilder.content {
            dam("sling:Folder") {
                image("dam:Asset") {
                    "jcr:content" {
                        renditions("nt:folder") {
                            original("nt:file") {
                                "jcr:content"("nt:resource", "jcr:data": "data")
                            }
                        }
                    }
                }
                "image-renditions"("dam:Asset") {
                    "jcr:content"("jcr:data": "data") {
                        renditions("nt:folder") {
                            original("nt:file") {
                                "jcr:content"("nt:resource", "jcr:data": "data")
                            }
                            one("nt:file") {
                                "jcr:content"("nt:resource", "jcr:data": "data")
                            }
                        }
                    }
                }
            }
        }
    }

    def "to string"() {
        setup:
        def node = getBasicNode("/content/lagers/jcr:content/stiegl")

        expect:
        node.toString() == "DefaultBasicNode{path=/content/lagers/jcr:content/stiegl, properties={sling:resourceType=de, jcr:primaryType=nt:unstructured}}"
    }

    def "get id"() {
        setup:
        def node = getBasicNode(path)

        expect:
        node.id == id

        where:
        path                                          | id
        "/content/citytechinc"                        | "content-citytechinc"
        "/content/citytechinc/jcr:content"            | "content-citytechinc"
        "/content/citytechinc/jcr:content/malort/one" | "malort-one"
        "/"                                           | ""
    }

    def "as map"() {
        setup:
        def map = getBasicNode("/content/citytechinc/jcr:content").asMap()

        expect:
        map["jcr:title"] == "CITYTECH, Inc."
        map["otherPagePath"] == "/content/ales/esb"
    }

    def "get"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        node.get(propertyName, defaultValue) == result

        where:
        propertyName          | defaultValue | result
        "otherPagePath"       | ""           | "/content/ales/esb"
        "nonExistentProperty" | ""           | ""
    }

    def "get optional"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        node.get(propertyName, type).present == result

        where:
        propertyName          | type    | result
        "otherPagePath"       | String  | true
        "otherPagePath"       | Integer | false
        "nonExistentProperty" | String  | false
    }

    def "get as list"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        node.getAsList("multiValue", String) == ["one", "two"]
    }

    def "get as href"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        node.getAsHref(propertyName).get() == href

        where:
        propertyName    | href
        "otherPagePath" | "/content/ales/esb.html"
        "externalPath"  | "http://www.reddit.com"
    }

    def "get as href strict"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        node.getAsHref(propertyName, true).get() == href

        where:
        propertyName          | href
        "otherPagePath"       | "/content/ales/esb.html"
        "nonExistentPagePath" | "/content/home"
        "externalPath"        | "http://www.reddit.com"
    }

    def "get as href returns absent where appropriate"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        !node.getAsHref(propertyName).present

        where:
        propertyName << ["beer", ""]
    }

    def "get as mapped href"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        node.getAsHref("otherPagePath", false, true).get() == "/ales/esb.html"
    }

    def "get as mapped href strict"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        node.getAsHref(propertyName, true, true).get() == href

        where:
        propertyName          | href
        "otherPagePath"       | "/ales/esb.html"
        "nonExistentPagePath" | "/home"
        "externalPath"        | "http://www.reddit.com"
    }

    def "get as href for null"() {
        when:
        getBasicNode("/content/citytechinc/jcr:content").getAsHref(null)

        then:
        thrown NullPointerException
    }

    def "get as link"() {
        setup:
        def link = getBasicNode("/content/citytechinc/jcr:content").getAsLink("otherPagePath").get()

        expect:
        link.path == "/content/ales/esb"
    }

    def "get as link strict"() {
        setup:
        def link = getBasicNode("/content/citytechinc/jcr:content").getAsLink("nonExistentPagePath", true).get()

        expect:
        link.path == "/content/home"
        link.external
        link.extension == ""
    }

    @IgnoreRest
    def "get as mapped link"() {
        setup:
        def link = getBasicNode("/content/citytechinc/jcr:content").getAsLink("otherPagePath", false, true).get()

        expect:
        link.path == "/content/ales/esb"
        link.href == "/ales/esb.html"
    }

    def "get as mapped link strict"() {
        setup:
        def link = getBasicNode("/content/citytechinc/jcr:content").getAsLink("nonExistentPagePath", true, true).get()

        expect:
        link.path == "/home"
        link.external
        link.extension == ""
    }

    def "get as link for null"() {
        when:
        getBasicNode("/content/citytechinc/jcr:content").getAsLink(null)

        then:
        thrown NullPointerException
    }

    def "get as link for non-existent property"() {
        setup:
        def linkOptional = getBasicNode("/content/citytechinc/jcr:content").getAsLink("beer")

        expect:
        !linkOptional.present
    }

    def "get as page"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        node.getAsPage("otherPagePath").get().path == "/content/ales/esb"
        !node.getAsPage("nonExistentProperty").present
    }

    def "get as type"() {
        setup:
        def node = getBasicNode("/content/lagers/jcr:content/dynamo")

        expect:
        node.getAsType("related", type).present == result

        where:
        type          | result
        ComponentNode | true
        Asset         | false
    }

    def "get href"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        node.href == "/content/citytechinc/jcr:content.html"
    }

    def "get image reference"() {
        setup:
        def node = getBasicNode(path)

        expect:
        node.imageReference.present == isPresent

        where:
        path                               | isPresent
        "/content/citytechinc/jcr:content" | true
        "/content/ales/esb/jcr:content"    | false
    }

    def "get self image reference"() {
        setup:
        def node = getBasicNode(path)

        expect:
        node.getImageReference(isSelf).present == isPresent

        where:
        path                                       | isSelf | isPresent
        "/content/citytechinc/jcr:content"         | false  | true
        "/content/citytechinc/jcr:content"         | true   | true
        "/content/ales/esb/jcr:content"            | false  | false
        "/content/ales/esb/jcr:content"            | true   | false
        "/content/ales/esb/jcr:content/greeneking" | true   | false
        "/content/ales/esb/jcr:content/greeneking" | false  | true
    }

    def "get image source optional"() {
        setup:
        def node = getBasicNode(path)

        expect:
        node.imageSource.present == isPresent

        where:
        path                                       | isPresent
        "/content/citytechinc/jcr:content"         | true
        "/content/ales/esb/jcr:content"            | false
        "/content/citytechinc/jcr:content/beer"    | true
        "/content/citytechinc/jcr:content/whiskey" | false
        "/content/citytechinc/jcr:content/bourbon" | true
    }

    def "get image source"() {
        setup:
        def node = getBasicNode(path)

        expect:
        node.imageSource.get() == imageSrc

        where:
        path                                       | imageSrc
        "/content/citytechinc/jcr:content"         | "/content/citytechinc.img.png"
        "/content/citytechinc/jcr:content/beer"    | "/content/citytechinc/jcr:content/beer.img.png"
        "/content/citytechinc/jcr:content/bourbon" | "/content/citytechinc/jcr:content/bourbon.img.png"
    }

    def "get named image source"() {
        setup:
        def node = getBasicNode(path)

        expect:
        node.getImageSource(name).get() == imageSrc

        where:
        path                                    | name          | imageSrc
        "/content/citytechinc/jcr:content"      | "image"       | "/content/citytechinc.img.png"
        "/content/citytechinc/jcr:content"      | "secondimage" | "/content/citytechinc.img.secondimage.png"
        "/content/citytechinc/jcr:content/beer" | "image"       | "/content/citytechinc/jcr:content/beer.img.png"
        "/content/citytechinc/jcr:content/beer" | "secondimage" | "/content/citytechinc/jcr:content/beer.img.secondimage.png"
    }

    def "get image source with width"() {
        setup:
        def node = getBasicNode(path)

        expect:
        node.getImageSource(width).get() == imageSrc

        where:
        path                                    | width | imageSrc
        "/content/citytechinc/jcr:content"      | -1    | "/content/citytechinc.img.png"
        "/content/citytechinc/jcr:content"      | 100   | "/content/citytechinc.img.100.png"
        "/content/citytechinc/jcr:content/beer" | -1    | "/content/citytechinc/jcr:content/beer.img.png"
        "/content/citytechinc/jcr:content/beer" | 100   | "/content/citytechinc/jcr:content/beer.img.100.png"
    }

    def "get named image source with width"() {
        setup:
        def node = getBasicNode(path)

        expect:
        node.getImageSource(name, width).get() == imageSrc

        where:
        path                                    | name          | width | imageSrc
        "/content/citytechinc/jcr:content"      | "image"       | -1    | "/content/citytechinc.img.png"
        "/content/citytechinc/jcr:content"      | "image"       | 100   | "/content/citytechinc.img.100.png"
        "/content/citytechinc/jcr:content"      | "secondimage" | -1    | "/content/citytechinc.img.secondimage.png"
        "/content/citytechinc/jcr:content"      | "secondimage" | 100   | "/content/citytechinc.img.secondimage.100.png"
        "/content/citytechinc/jcr:content/beer" | "image"       | -1    | "/content/citytechinc/jcr:content/beer.img.png"
        "/content/citytechinc/jcr:content/beer" | "image"       | 100   | "/content/citytechinc/jcr:content/beer.img.100.png"
        "/content/citytechinc/jcr:content/beer" | "secondimage" | -1    | "/content/citytechinc/jcr:content/beer.img.secondimage.png"
        "/content/citytechinc/jcr:content/beer" | "secondimage" | 100   | "/content/citytechinc/jcr:content/beer.img.secondimage.100.png"
    }

    def "get index"() {
        setup:
        def node = getBasicNode("/content/ales/esb/jcr:content/morland")

        expect:
        node.index == 1
    }

    def "get index for resource type"() {
        setup:
        def node = getBasicNode("/content/lagers/jcr:content/stiegl")

        expect:
        node.getIndex("de") == 0
    }

    def "get named image reference"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        node.getImageReference("nsfwImage").get() == "omg.png"
        !node.getImageReference("sfwImage").present
    }

    def "get link"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        node.link.path == "/content/citytechinc/jcr:content"
    }

    def "get link builder"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        node.linkBuilder.build().path == "/content/citytechinc/jcr:content"
    }

    def "get node"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        node.node.get().path == "/content/citytechinc/jcr:content"
    }

    def "get path"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        node.path == "/content/citytechinc/jcr:content"
    }

    def "get properties"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content/beer")
        def predicate = new PropertyNamePredicate("label")

        expect:
        node.getProperties(predicate)*.name == ["label"]
    }

    def "get properties for null node returns empty list"() {
        setup:
        def resource = new NonExistingResource(resourceResolver, "/content/non-existing")
        def node = new DefaultBasicNode(resource)

        expect:
        !node.getProperties(Predicates.alwaysTrue())
    }

    def "get resource"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        node.resource.path == "/content/citytechinc/jcr:content"
    }

    def "has image"() {
        setup:
        def node = getBasicNode(path)

        expect:
        node.hasImage == hasImage

        where:
        path                               | hasImage
        "/content/citytechinc/jcr:content" | true
        "/content/ales/esb/jcr:content"    | false
    }

    def "has named image"() {
        setup:
        def node = getBasicNode(path)

        expect:
        node.isHasImage(name) == hasImage

        where:
        path                               | name          | hasImage
        "/content/citytechinc/jcr:content" | "image"       | true
        "/content/citytechinc/jcr:content" | "secondimage" | true
        "/content/citytechinc/jcr:content" | "thirdimage"  | false
        "/content/citytechinc/jcr:content" | "fourthimage" | false
        "/content/ales/esb/jcr:content"    | "image"       | false
    }

    def "get image rendition returns absent"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        !node.getImageRendition("sfwImage", "").present
    }

    def "get image rendition"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        !node.getImageRendition("").present
    }

    def "get named image rendition"() {
        setup:
        def node = getBasicNode("/content/citytechinc/jcr:content")

        expect:
        node.getImageRendition(name, renditionName).present == result

        where:
        name                  | renditionName | result
        "secondimage"         | ""            | false
        "imageWithRenditions" | "one"         | true
        "imageWithRenditions" | "four"        | false
    }

    def getBasicNode(String path) {
        def resource = resourceResolver.getResource(path)

        new DefaultBasicNode(resource)
    }
}
