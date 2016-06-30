package com.icfolson.aem.library.core.tags

import com.icfolson.aem.library.core.specs.AemLibrarySpec
import com.icfolson.aem.prosper.traits.JspTagTrait
import spock.lang.Unroll

@Unroll
class HrefTagSpec extends AemLibrarySpec implements JspTagTrait {

    def setupSpec() {
        pageBuilder.content {
            citytechinc {
                "jcr:content"(path: "/content/global")
                ctmsp()
            }
        }
    }

    def "href for property"() {
        setup:
        def proxy = init(HrefTag, "/content/citytechinc/jcr:content")

        (proxy.tag as HrefTag).propertyName = "path"

        when:
        proxy.tag.doEndTag()

        then:
        proxy.output == "/content/global.html"
    }

    def "href for inherited property"() {
        setup:
        def proxy = init(HrefTag, "/content/citytechinc/ctmsp/jcr:content")
        def tag = proxy.tag as HrefTag

        tag.with {
            propertyName = testPropertyName
            inherit = String.valueOf(testInherit)
        }

        when:
        tag.doEndTag()

        then:
        proxy.output == output

        where:
        testPropertyName  | testInherit | output
        "path"            | false       | ""
        "path"            | true        | "/content/global.html"
        "nonExistentPath" | false       | ""
        "nonExistentPath" | true        | ""
    }
}