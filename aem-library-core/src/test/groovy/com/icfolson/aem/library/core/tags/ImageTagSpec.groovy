package com.icfolson.aem.library.core.tags

import com.citytechinc.aem.prosper.traits.JspTagTrait
import com.icfolson.aem.library.core.specs.AemLibrarySpec
import spock.lang.Unroll

@Unroll
class ImageTagSpec extends AemLibrarySpec implements JspTagTrait {

    def setupSpec() {
        pageBuilder.content {
            citytechinc {
                "jcr:content" {
                    component("jcr:title": "Component", fileReference: "/content/dam/image") {
                        image(fileReference: "/content/dam/image")
                        noimage()
                    }
                }
            }
        }

        nodeBuilder.content {
            dam("sling:Folder") {
                image("dam:Asset") {
                    "jcr:content"("jcr:data": "data")
                }
            }
        }
    }

    def "draw image for current resource"() {
        setup:
        def proxy = init(ImageTag, "/content/citytechinc/jcr:content/component")
        def tag = proxy.tag as ImageTag

        tag.title = title
        tag.alt = alt

        when:
        tag.doEndTag()

        then:
        proxy.output == output

        where:
        title   | alt   | output
        "Title" | ""    | '<img src="/content/dam/image" alt="Title" title="Title" >'
        "Title" | "Alt" | '<img src="/content/dam/image" alt="Alt" title="Title" >'
    }

    def "draw image for named resource"() {
        setup:
        def proxy = init(ImageTag, "/content/citytechinc/jcr:content/component")
        def tag = proxy.tag as ImageTag

        tag.name = "image"
        tag.title = title
        tag.alt = alt

        when:
        tag.doEndTag()

        then:
        proxy.output == output

        where:
        title   | alt   | output
        "Title" | ""    | '<img src="/content/dam/image" alt="Title" title="Title" >'
        "Title" | "Alt" | '<img src="/content/dam/image" alt="Alt" title="Title" >'
    }

    def "no image"() {
        setup:
        def proxy = init(ImageTag, "/content/citytechinc/jcr:content/component")
        def tag = proxy.tag as ImageTag

        tag.name = "noimage"

        when:
        tag.doEndTag()

        then:
        !proxy.output
    }
}
